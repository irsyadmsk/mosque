async page => {
  const errors = [], saves = [], actions = [];
  const fixture = await page.request.get('http://127.0.0.1:8092/admin-review-status.json').then(r => r.json());
  page.on('pageerror', e => errors.push(e.message));
  await page.route('**/api/**', async route => {
    const path = new URL(route.request().url()).pathname;
    if (path === '/api/save-config') saves.push(route.request().postDataJSON());
    if (path.startsWith('/api/trigger-')) actions.push(path);
    await route.fulfill({contentType:'application/json',body:JSON.stringify(path === '/api/status' ? fixture : {status:'ok',authenticated:true,message:'Pengaturan disimpan'})});
  });
  await page.goto('http://127.0.0.1:8092/admin-review.html');
  await page.evaluate(() => localStorage.setItem('masjidku_session_token','browser-review-only'));
  await page.evaluate(data => { showDashboard(); renderData(data); switchTab('tab-waktu'); },fixture);
  if (await page.locator('#adhanDurationSeconds').inputValue() !== '180') throw new Error('Adhan config not populated');
  if (Number(await page.locator('#cfgLat2').inputValue()) !== 0) throw new Error('Equator coordinate replaced by fallback');
  if (Number(await page.locator('#cfgTimezone2').inputValue()) !== 0) throw new Error('Stored timezone overwritten by map');
  if (await page.locator('#iqJumat').inputValue() !== '0') throw new Error('Zero-minute iqomah replaced by fallback');
  await page.locator('#adhanDurationSeconds').fill('240');
  await page.locator('#fridayKhutbahMinutes').fill('45');
  await page.locator('#fridayKhutbahManual').check();
  await page.locator('#focusBlankScreen').uncheck();
  await page.locator('#formWaktu button[type="submit"]').click();
  await page.waitForTimeout(100);
  if (!saves.length || saves.at(-1).adhanDurationSeconds !== '240' || saves.at(-1).fridayKhutbahManual !== true || saves.at(-1).iqomahJumat !== '0') throw new Error('Worship save payload mismatch');
  await page.locator('button[onclick="triggerAction(\'khutbah\')"]').click();
  await page.waitForTimeout(100);
  if (!actions.includes('/api/trigger-khutbah')) throw new Error('Khutbah button does not reach API');
  for (const width of [1440,390]) {
    await page.setViewportSize({width,height:1100});
    if (!await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)) throw new Error(`Admin overflow ${width}`);
  }
  if (errors.length) throw new Error(JSON.stringify(errors));
  await page.screenshot({path:'C:/Users/T6N0CV02S35823C/Github/mosque/.redesign-preview/admin-worship-mobile.png',fullPage:true});
  return {embeddedAdmin:'passed',configHydration:'passed',savePayload:'passed',khutbahAction:'passed',exceptions:errors};
}
