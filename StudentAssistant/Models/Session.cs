using System;			

namespace StudentAssistant.Models
{
	public class Session
	{
		public int SessionId { get; set; }
		public string Type { get; set; }
		public bool Status { get; set; }
		public int Mark { get; set; }
		public DateTime Date { get; set; }
	}
}