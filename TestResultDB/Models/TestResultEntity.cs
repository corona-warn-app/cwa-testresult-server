using System;
using System.Collections.Generic;

namespace CWA.TestResultDB.Models
{
    public partial class TestResultEntity
    {
        public Guid Id { get; set; }
        public string ResultId { get; set; }
        public DateTime ResultDate { get; set; }
        public int Result { get; set; }
    }
}
