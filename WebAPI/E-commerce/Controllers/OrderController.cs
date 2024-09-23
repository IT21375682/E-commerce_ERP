using Microsoft.AspNetCore.Mvc;

namespace E_commerce.Controllers
{
    public class OrderController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
