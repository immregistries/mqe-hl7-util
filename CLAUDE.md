# mqe-hl7-util

Parses HL7 v2 VXU (vaccination update) messages into the data model used by `mqe-validator` to validate
immunization data quality. See `../CLAUDE.md` for how this fits with the other MQE repos.

- GitHub: immregistries/mqe-hl7-util
- Maven: `groupId org.immregistries`, `artifactId mqe-hl7-utility`, packaging `jar`
- Java: 1.8
- Published to Maven Central via OSSRH (Sonatype) — has javadoc/source jars and GPG signing wired into the build
- Key deps: commons-lang3, commons-io, commons-collections4, slf4j, joda-time, `codebase-client`
- Consumed by: `mqe-validator`, `mqe-message-hub`, and externally by the **Data-at-Rest (DAR)** project
- Status: actively reused as a library; last commit 2023-11-02
