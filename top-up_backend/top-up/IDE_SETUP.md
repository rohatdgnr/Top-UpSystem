# IDE Setup Instructions

## IntelliJ IDEA

1. **Enable Annotation Processing:**
   - File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"
   - Set "Obtain processors from project classpath"
   - Set "Store generated sources relative to: Module content root"
   - Generated sources directory: `target/generated-sources/annotations`

2. **Invalidate Caches:**
   - File → Invalidate Caches / Restart
   - Select "Invalidate and Restart"

3. **Maven Settings:**
   - File → Settings → Build, Execution, Deployment → Build Tools → Maven
   - Ensure "Use Maven wrapper" is checked
   - Ensure "Delegate IDE build/run actions to Maven" is checked

4. **Project Structure:**
   - File → Project Structure → Project
   - Set Project SDK to Java 17
   - Set Project language level to 17

## Eclipse

1. **Enable Annotation Processing:**
   - Project → Properties → Java Compiler → Annotation Processing
   - Check "Enable annotation processing"
   - Set "Generated source directory" to `target/generated-sources/annotations`

2. **Clean Project:**
   - Project → Clean → Clean all projects

## VS Code

1. Install extensions:
   - Java Extension Pack
   - Lombok Annotations Support

2. Settings (settings.json):
```json
{
  "java.configuration.updateBuildConfiguration": "automatic",
  "java.compile.nullAnalysis.mode": "automatic"
}
```

## General Troubleshooting

If you still see `ExceptionInInitializerError` or `TypeTag :: UNKNOWN`:

1. **Delete IDE cache:**
   - IntelliJ: `.idea` folder
   - Eclipse: `.metadata` folder
   - VS Code: `.vscode` folder

2. **Reimport Maven project:**
   - IntelliJ: Right-click on `pom.xml` → Maven → Reload Project
   - Eclipse: Right-click on project → Maven → Update Project

3. **Rebuild project:**
   ```bash
   mvnw clean install -DskipTests
   ```

4. **Check Java version:**
   ```bash
   java -version  # Should be 17 or higher
   ```

