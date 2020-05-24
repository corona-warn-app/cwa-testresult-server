using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Covid19LabResultsService.Models
{
    /// <summary>
    /// Request the test result
    /// </summary>
    public class TestResultRequestApiModel
    {
        /// <summary>
        /// Hash (SHA256) of test result id (aka QR-Code, GUID) encoded as hexstring<br/>
        ///    9cdda87d-a833-4f36-bd55-98b7ff8c81f3 -> 75ab05c2a8f0da58737934b55a96f86df4609458bf6360c1de2010f5edd7fca5 
        /// </summary>
        [Required]
        [StringLength(64, MinimumLength = 64)]
        [RegularExpression(@"^([A-Fa-f0-9]{2}){32}$")]
        public string Id { get; set; }
    }
}
