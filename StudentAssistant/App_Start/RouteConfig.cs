using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;
using System.Web.Http;


namespace StudentAssistant
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

			routes.MapHttpRoute(
	            name: "DefaultApi",
	            routeTemplate: "api/{controller}/{action}/{id}",
	            defaults: new { controller = "Rest", id = RouteParameter.Optional }
            );

			routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "Home", action = "MainPage", id = UrlParameter.Optional }
            );
        }
    }
}
