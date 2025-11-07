Param(
  [string]$BaseUrl = "http://localhost:8080/api"
)

Write-Host "Seeding phoneshop data via $BaseUrl" -ForegroundColor Cyan

$brands = @(
  @{ name = "Aurora" },
  @{ name = "Pulse" },
  @{ name = "Nimbus" }
)

$brandMap = @{}

foreach ($brand in $brands) {
  try {
    $response = Invoke-RestMethod -Method Post -Uri "$BaseUrl/brands" -ContentType 'application/json' -Body ($brand | ConvertTo-Json)
    $brandMap[$response.name] = $response.id
    Write-Host "Created brand $($response.name) (#$($response.id))" -ForegroundColor Green
  }
  catch {
    Write-Warning "Failed to create brand $($brand.name): $($_.Exception.Message)"
  }
}

$phones = @(
  @{ model = "Aurora X1"; price = 1099.99; stock = 25; specs = "6.7\" OLED, 12GB RAM, 256GB, 5000mAh, 48MP triple"; brand = "Aurora" },
  @{ model = "Aurora Nova"; price = 899.00; stock = 18; specs = "6.4\" AMOLED, 8GB RAM, 256GB, 4600mAh, 64MP"; brand = "Aurora" },
  @{ model = "Pulse Edge"; price = 1199.50; stock = 12; specs = "6.8\" LTPO, 16GB RAM, 512GB, 5200mAh, 50MP"; brand = "Pulse" },
  @{ model = "Pulse Air"; price = 749.99; stock = 30; specs = "6.1\" OLED, 8GB RAM, 128GB, 4300mAh, 48MP"; brand = "Pulse" },
  @{ model = "Nimbus Fold"; price = 1399.00; stock = 8; specs = "7.1\" foldable AMOLED, 12GB RAM, 512GB, 4800mAh, 108MP"; brand = "Nimbus" },
  @{ model = "Nimbus Lite"; price = 549.00; stock = 40; specs = "6.2\" IPS, 6GB RAM, 128GB, 4500mAh, 48MP"; brand = "Nimbus" }
)

foreach ($phone in $phones) {
  if (-not $brandMap.ContainsKey($phone.brand)) {
    Write-Warning "Skipping phone $($phone.model) because brand $($phone.brand) was not created."
    continue
  }

  $payload = [ordered]@{
    model = $phone.model
    price = $phone.price
    stock = $phone.stock
    specs = $phone.specs
    brand = @{ id = $brandMap[$phone.brand] }
  }

  try {
    $response = Invoke-RestMethod -Method Post -Uri "$BaseUrl/phones" -ContentType 'application/json' -Body ($payload | ConvertTo-Json)
    Write-Host "Created phone $($response.model)" -ForegroundColor Green
  }
  catch {
    Write-Warning "Failed to create phone $($phone.model): $($_.Exception.Message)"
  }
}

Write-Host "Seed completed." -ForegroundColor Cyan

