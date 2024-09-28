using MongoDB.Driver;
using E_commerce.Repositories;
using E_commerce.Services;

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

// Register repositories
builder.Services.AddScoped<IUserRepository>(); // Registering the concrete user repository
builder.Services.AddScoped<IProductRepository>(); // Registering the concrete product repository
builder.Services.AddScoped<IOrderRepository>(); // Registering the concrete order repository



// Register services
builder.Services.AddScoped<UserService>(); // Registering the UserService
builder.Services.AddScoped<ProductService>(); // Registering the Product Service
builder.Services.AddScoped<OrderService>(); // Registering the Order Service

// Add services to the container.
builder.Services.AddControllers();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

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
