async page => {
  const errors = [];
  page.on('pageerror', error => errors.push(error.message));
  await page.goto('http://127.0.0.1:8091/.redesign-preview/');
  await page.locator('[data-worship="khutbah"]').click();
  if (!await page.locator('#focusPreview').isVisible()) throw new Error('Khutbah overlay missing');
  if (!await page.locator('#focusTitle').textContent().then(s => s.includes('Khutbah'))) throw new Error('Khutbah message missing');
  await page.locator('#focusBlank').check();
  if (await page.locator('#focusTitle').textContent() !== '') throw new Error('Blank mode retains message');
  if (!await page.locator('#focusClock').isHidden()) throw new Error('Blank mode retains clock');
  await page.locator('#focusBlank').uncheck();
  await page.locator('#focusClockEnabled').uncheck();
  if (!await page.locator('#focusClock').isHidden()) throw new Error('Clock setting ignored');
  for (const mode of ['adhan','iqomah','prayer']) {
    await page.locator(`[data-worship="${mode}"]`).click();
    if (!await page.locator('#focusPreview').isVisible()) throw new Error(`Missing ${mode}`);
  }
  for (const width of [1440,768,390]) {
    await page.setViewportSize({width,height:1100});
    if (!await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)) throw new Error(`Overflow ${width}`);
  }
  await page.locator('[data-worship="normal"]').click();
  if (!await page.locator('#focusPreview').isHidden()) throw new Error('Normal mode failed');
  await page.locator('[data-panel="identity"]').click();
  if (!await page.locator('#worship-controls').isVisible()) throw new Error('Worship controls disappear on tab change');
  if (errors.length) throw new Error(JSON.stringify(errors));
  await page.setViewportSize({width:1440,height:1100});
  await page.locator('[data-worship="khutbah"]').click();
  await page.locator('#tv').screenshot({path:'C:/Users/T6N0CV02S35823C/Github/mosque/.redesign-preview/khutbah-focus.png'});
  return {modes:5,blankAndClock:'passed',viewports:[1440,768,390],exceptions:errors};
}
