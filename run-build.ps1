Set-Location "c:\Users\gurua\Documents\AASHWIN MART"
$result = & mvn clean package -B -DskipTests 2>&1
$result | Out-File -FilePath "build-output.txt" -Encoding UTF8
$exitCode = $LASTEXITCODE
Add-Content -Path "build-output.txt" -Value "BUILD_EXIT_CODE:$exitCode"
