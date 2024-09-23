using Microsoft.AspNetCore.Mvc;

namespace E_commerce.Controllers
{
    public class ProductController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
