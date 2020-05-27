using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Security.Cryptography.X509Certificates;
using Microsoft.Extensions.Configuration;

namespace CWA.TestResultsService
{
    public class ApiClientCertificateValidationService
    {
        List<string> validThumbprints = new List<string>();

        public ApiClientCertificateValidationService(IConfiguration configuration)
        {
            var thumbprints = configuration.GetValue<string>("ValidThumbprints");
            if(!String.IsNullOrWhiteSpace(thumbprints))
            {
                validThumbprints = thumbprints
                    .Split(",;".ToCharArray())
                    .Select(i => i.Trim())
                    .Where(i => !String.IsNullOrWhiteSpace(i))
                    .ToList();
            }
        }

        public bool ValidateCertificate(X509Certificate2 clientCertificate)
        {
            return validThumbprints.Contains(clientCertificate.Thumbprint, StringComparer.InvariantCultureIgnoreCase);
        }
    }
}
