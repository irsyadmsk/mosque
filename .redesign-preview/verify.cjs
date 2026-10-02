async page => {
  const failures = [];
  const results = [];
  const check = (condition, message) => { if (!condition) failures.push(message); else results.push(message); };
  const errors = [];
  page.on('pageerror', error => errors.push(error.message));
  await page.goto('http://127.0.0.1:8091/.redesign-preview/');
  for (const [width, height] of [[1440, 1100], [768, 1024], [390, 844]]) {
    await page.setViewportSize({ width, height });
    check(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth), `No page overflow at ${width}px`);
  }
  for (const direction of ['mihrab', 'grid', 'board']) {
    await page.locator(`.direction[data-direction="${direction}"]`).click();
    check(await page.locator('#tv').getAttribute('data-direction') === direction, `Layout ${direction} switches`);
  }
  await page.locator('.builtin-wallpapers summary').click();
  await page.locator('[data-wallpaper="midnight_kiswah"]').click();
  check((await page.locator('#wallpaper').getAttribute('style')).includes('midnight_kiswah'), 'Wallpaper switches');
  await page.locator('#dim').fill('60');
  check(await page.locator('#dimValue').textContent() === '60%', 'Dim value updates');
  await page.locator('[data-panel="identity"]').click();
  await page.locator('#name').fill('Masjid Uji <script>');
  check(await page.locator('#mosqueName').textContent() === 'Masjid Uji <script>', 'Identity updates as text');
  check(await page.locator('#mosqueName script').count() === 0, 'Input cannot inject markup');
  await page.locator('[data-panel="content"]').click();
  await page.locator('#announcementInput').fill('Maklumat uji');
  check(await page.locator('#announcement').textContent() === 'Maklumat uji', 'Announcement updates');
  await page.locator('#showInformation').uncheck();
  check(await page.locator('#information').isHidden(), 'Information visibility changes');
  await page.locator('#showInformation').check();
  await page.locator('[data-panel="appearance"]').click();
  await page.emulateMedia({ reducedMotion: 'reduce' });
  await page.locator('#replay').click();
  check(await page.locator('#slideStatus').textContent() === 'Laporan kas', 'Slide updates with reduced motion');
  check(await page.locator('#information').evaluate(el => getComputedStyle(el).transform) === 'none', 'Reduced motion does not move information');
  await page.emulateMedia({ reducedMotion: 'no-preference' });
  await page.locator('#reduceMotion').check();
  await page.locator('#replay').click();
  check(await page.locator('#information').evaluate(el => getComputedStyle(el).transform) === 'none', 'Manual reduced motion stops animation');
  check(errors.length === 0, `No JavaScript errors (${errors.length})`);
  if (failures.length) throw new Error(JSON.stringify({ failures, results }));
  return { passed: results.length, results };
}
