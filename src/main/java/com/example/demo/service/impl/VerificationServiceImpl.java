@Service
public class VerificationServiceImpl implements VerificationService {

    private final CertificateRepository certificateRepository;
    private final VerificationLogRepository logRepository;

    public VerificationServiceImpl(CertificateRepository certificateRepository,
                                   VerificationLogRepository logRepository) {
        this.certificateRepository = certificateRepository;
        this.logRepository = logRepository;
    }

    @Override
    public VerificationLog verifyCertificate(String code, String ipAddress) {

        Optional<Certificate> certOpt =
                certificateRepository.findByVerificationCode(code);

        VerificationLog log = VerificationLog.builder()
                .verifiedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .build();

        if (certOpt.isPresent()) {
            log.setCertificate(certOpt.get());
            log.setStatus("SUCCESS");
        } else {
            log.setStatus("FAILED");
        }

        return logRepository.save(log);
    }

    @Override
    public List<VerificationLog> getLogsByCertificate(Long certificateId) {
        return logRepository.findAll().stream()
                .filter(l -> l.getCertificate() != null
                        && l.getCertificate().getId().equals(certificateId))
                .toList();
    }
}
