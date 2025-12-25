@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    public CertificateController(CertificateService s) { this.certificateService = s; }

    @PostMapping("/generate/{studentId}/{templateId}")
    public ResponseEntity<Certificate> generate(@PathVariable Long studentId, @PathVariable Long templateId) {
        return ResponseEntity.ok(certificateService.generateCertificate(studentId, templateId));
    }

    @GetMapping("/{certificateId}")
    public ResponseEntity<Certificate> get(@PathVariable Long certificateId) {
        return ResponseEntity.ok(certificateService.getCertificate(certificateId));
    }
}