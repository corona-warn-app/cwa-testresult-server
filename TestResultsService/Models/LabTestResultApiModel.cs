using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CWA.TestResultsService.Models
{
    /// <summary>
    /// Lab test result 
    /// </summary>
    public class LabTestResultApiModel
    {
        /// <summary>
        /// Hash (SHA256) of test result id (aka QR-Code, GUID) encoded as hexstring<br/>
        /// </summary>
        [Required]
        [StringLength(64, MinimumLength = 64)]
        [RegularExpression(@"^([A-Fa-f0-9]{2}){32}$")]
        public string Id { get; set; }

        /// <summary>
        /// Result:<br/>
        ///     1: negative<br/>
        ///     2: positive<br/>
        ///     3: invalid<br/>
        /// </summary>
        [Required]
        [Range(1,3)]
        public int Result { get; set; }
    }
}
