using MongoDB.Driver;
using E_commerce.Repositories;
using E_commerce.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;

var builder = WebApplication.CreateBuilder(args);

// Add MongoDB connection
builder.Services.AddSingleton<IMongoClient, MongoClient>(sp =>
{
    var connectionString = builder.Configuration.GetConnectionString("MongoDB");
    return new MongoClient(connectionString);
});

builder.Services.AddScoped(sp =>
{
    var client = sp.GetRequiredService<IMongoClient>();
    return client.GetDatabase("ECommerceDB");
});

// Add CORS services
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowSpecificOrigin",
        policy =>
        {
            policy.AllowAnyOrigin() // React app URL
                  .AllowAnyHeader()
                  .AllowAnyMethod();
        });
});


// Register repositories
builder.Services.AddScoped<IUserRepository>(); // Registering the concrete user repository
builder.Services.AddScoped<IProductRepository>(); // Registering the concrete product repository
builder.Services.AddScoped<ICategoryRepository>(); // Registering the concrete repository
builder.Services.AddScoped<IOrderRepository>(); // Registering the concrete order repository



// Register services
builder.Services.AddScoped<UserService>(); // Registering the UserService
builder.Services.AddScoped<ProductService>(); // Registering the Product Service
builder.Services.AddScoped<OrderService>(); // Registering the Order Service
builder.Services.AddScoped<CategoryService>(); // Registering the UserService

builder.Services.AddScoped<JwtService>(sp =>
{
    var config = builder.Configuration;
    return new JwtService(config["Jwt:Key"]); // Add Jwt:Key to your appsettings
});

// JWT Authentication
var jwtSettings = builder.Configuration.GetSection("Jwt");
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = false,
        ValidateAudience = false,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtSettings["Key"]))
    };
});




// Add services to the container.
builder.Services.AddControllers();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();
app.UseCors("AllowSpecificOrigin");
// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
