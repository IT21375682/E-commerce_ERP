using MongoDB.Driver;
using E_commerce.Repositories;
using E_commerce.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using E_commerce.Models;

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

// Register IMongoCollection<Order>
builder.Services.AddScoped(sp =>
{
    var database = sp.GetRequiredService<IMongoDatabase>();
    return database.GetCollection<Order>("Orders"); // Ensure "Orders" matches your MongoDB collection name
});
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
builder.Services.AddScoped<ICommentRepository>(); // Registering the concrete comment repository
builder.Services.AddScoped<ICartRepository>(); // Registering the concrete cart repository


// Register services
builder.Services.AddScoped<UserService>(); // Registering the UserService
builder.Services.AddScoped<ProductService>(); // Registering the Product Service
builder.Services.AddScoped<OrderService>(); // Registering the Order Service
builder.Services.AddScoped<CategoryService>(); // Registering the UserService
builder.Services.AddScoped<EmailService>(); // Registering the EmailService
builder.Services.AddScoped<CommentService>(); // Registering the CommentService
builder.Services.AddScoped<CartService>(); // Registering the CartService


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

// Add controllers and views
builder.Services.AddControllersWithViews();

// Bind the server to all network interfaces (0.0.0.0) to make it accessible externally
builder.WebHost.UseUrls("https://localhost:5004");



// Add services to the container.
builder.Services.AddControllers();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();
app.UseCors("AllowSpecificOrigin");
// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment() || true)
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// app.UseHttpsRedirection(); // Uncomment if you plan to use HTTPS
app.UseStaticFiles();
app.UseRouting();

// Apply CORS policy
app.UseCors("AllowSpecificOrigin");

app.UseAuthorization();

app.MapControllers();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
