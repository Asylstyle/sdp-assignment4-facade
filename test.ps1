$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force out | Out-Null
    javac --release 17 -Xlint:all -d out src/ordering/*.java tests/ordering/*.java
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
    foreach ($testClass in @('SubsystemTests', 'OrderFacadeTests', 'ValidationTests')) {
        java -cp out "ordering.$testClass"
        if ($LASTEXITCODE -ne 0) { throw "Failed: $testClass" }
    }
    Write-Output 'All 3 test suites passed.'
} finally { Pop-Location }
