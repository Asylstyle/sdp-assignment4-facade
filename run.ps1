$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force out | Out-Null
    javac --release 17 -Xlint:all -d out src/ordering/*.java
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
    java -cp out ordering.Client
    if ($LASTEXITCODE -ne 0) { throw 'Demo failed' }
} finally { Pop-Location }
