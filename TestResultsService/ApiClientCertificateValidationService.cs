using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Security.Cryptography.X509Certificates;

namespace CWA.TestResultsService
{
    public class ApiClientCertificateValidationService
    {
        public bool ValidateCertificate(X509Certificate2 clientCertificate)
        {
            //var cert = new X509Certificate2(Path.Combine("sts_dev_cert.pfx"), "1234");
            //if (clientCertificate.Thumbprint == cert.Thumbprint)
            //{
            //    return true;
            //}

            return true;
        }
    }
}
