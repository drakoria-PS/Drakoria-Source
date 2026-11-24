param(
    [Parameter(Mandatory=$true)]
    [string]$SourceRepo
)
$ErrorActionPreference = 'Stop'
if (-not (Test-Path -LiteralPath (Join-Path $SourceRepo '.git'))) {
    throw 'DRAKORIA_SOURCE_GIT_REPOSITORY_REQUIRED'
}
$remote = (& git -C $SourceRepo remote get-url origin).Trim()
if ($LASTEXITCODE -ne 0 -or $remote -notmatch 'Drakoria-Source(?:\.git)?$') {
    throw 'DRAKORIA_SOURCE_REMOTE_MISMATCH'
}
$dirty = @(& git -C $SourceRepo status --porcelain)
if ($LASTEXITCODE -ne 0 -or $dirty.Count -gt 0) {
    throw 'DRAKORIA_SOURCE_LOCAL_CHANGES_PRESERVED'
}
$branch = 'feature-drakoria-range-mage-vfx-20261124'
& git -C $SourceRepo fetch origin $branch
if ($LASTEXITCODE -ne 0) { throw 'DRAKORIA_SOURCE_FETCH_FAILED' }
$current = (& git -C $SourceRepo branch --show-current).Trim()
if ($current -ne $branch) {
    if (@(& git -C $SourceRepo branch --list $branch).Count -gt 0) {
        & git -C $SourceRepo switch $branch
    } else {
        & git -C $SourceRepo switch --track -c $branch "origin/$branch"
    }
    if ($LASTEXITCODE -ne 0) { throw 'DRAKORIA_SOURCE_SWITCH_FAILED' }
}
& git -C $SourceRepo pull --ff-only origin $branch
if ($LASTEXITCODE -ne 0) { throw 'DRAKORIA_SOURCE_PULL_FAILED' }
Push-Location $SourceRepo
try {
    $javaVersion = (& java -version 2>&1 | Select-Object -First 1).ToString()
    if ($javaVersion -notmatch '"21') {
        throw 'DRAKORIA_SOURCE_JAVA21_REQUIRED'
    }
    $gradle = Get-Command gradle -ErrorAction SilentlyContinue
    if ($null -eq $gradle) {
        throw 'DRAKORIA_GRADLE8_REQUIRED_USE_EXISTING_GRADLE8148'
    }
    & gradle --no-daemon classes
    if ($LASTEXITCODE -ne 0) { throw 'DRAKORIA_SOURCE_COMPILE_FAILED' }
    $out = 'build\test-custom-combat-visuals'
    New-Item -ItemType Directory -Path $out -Force | Out-Null
    & javac --release 8 -encoding UTF-8 -cp 'build\classes\java\main' -d $out 'tests\com\skyfire\world\content\combat\visuals\DrakoriaCombatVisualsCheck.java'
    if ($LASTEXITCODE -ne 0) { throw 'DRAKORIA_VISUALS_TEST_COMPILE_FAILED' }
    & java -cp "$out;build\classes\java\main" com.skyfire.world.content.combat.visuals.DrakoriaCombatVisualsCheck
    if ($LASTEXITCODE -ne 0) { throw 'DRAKORIA_VISUALS_CPU_CHECK_FAILED' }
    Write-Host 'DRAKORIA_RANGE_MAGE_VISUALS_BUILD=PASS'
    Write-Host 'GPU_VISUAL_RUNTIME=NOT_TESTED'
    Write-Host 'Start server with -Ddrakoria.combat.visuals.range=true -Ddrakoria.combat.visuals.mage=true'
} finally {
    Pop-Location
}
