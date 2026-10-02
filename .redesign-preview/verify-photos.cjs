async page => {
  const failures = [];
  const errors = [];
  page.on('pageerror', error => errors.push(error.message));
  const check = (condition, message) => { if (!condition) failures.push(message); };
  await page.goto('http://127.0.0.1:8091/.redesign-preview/');
  await page.locator('.photo-choice').last().waitFor();
  await page.waitForFunction(() => document.getElementById('photoStatus').textContent === 'Foto aktif: Roni Supriyanto');
  check(await page.locator('.photo-choice').count() === 15, '15 photos listed');
  check(await page.locator('.photo-choice:disabled').count() === 0, 'All 15 originals found');
  const photos = await page.evaluate(async () => (await fetch('/.redesign-preview/photos.json')).json());
  for (const photo of photos) {
    const response = await page.request.get(`http://127.0.0.1:8091${photo.url}`);
    check(response.ok(), `${photo.id} image available`);
    check(response.headers()['content-type'] === 'image/jpeg', `${photo.id} valid MIME`);
    const thumbnail = await page.request.get(`http://127.0.0.1:8091${photo.thumbnail}`);
    check(thumbnail.ok(), `${photo.id} thumbnail available`);
  }
  await page.locator('[data-photo="nofianto"]').click();
  await page.waitForFunction(() => document.getElementById('photoStatus').textContent === 'Foto aktif: Nofianto');
  check((await page.locator('#wallpaper').getAttribute('style')).includes('/photos/nofianto.jpg'), 'Selected photo visible');
  await page.locator('#photoPosition').fill('65');
  check(await page.locator('#positionValue').textContent() === '65%', 'Position value updates');
  await page.locator('#photoFit').selectOption('contain');
  check(await page.locator('#photoPosition').isDisabled(), 'Position disabled for full photo');
  check(await page.locator('#wallpaper').evaluate(el => getComputedStyle(el).backgroundSize) === 'contain', 'Full photo fit applied');
  await page.locator('#photoFit').selectOption('cover');
  check(await page.locator('#photoPosition').isEnabled(), 'Position enabled for fill screen');
  await page.locator('[data-photo="roni"]').click();
  await page.waitForFunction(() => document.getElementById('photoStatus').textContent === 'Foto aktif: Roni Supriyanto');
  for (const width of [1440, 768, 390]) {
    await page.setViewportSize({width, height: 1000});
    check(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth), `No overflow at ${width}`);
  }
  const blocked = await page.request.get('http://127.0.0.1:8091/.redesign-preview/photos/unknown.jpg');
  check(blocked.status() === 404, 'Unknown photos are not served');
  check(errors.length === 0, 'No JavaScript exceptions');
  await page.setViewportSize({width:1440,height:1100});
  await page.evaluate(() => window.scrollTo(0,0));
  await page.screenshot({path:'.redesign-preview/wallpaper-desktop.png'});
  await page.setViewportSize({width:390,height:844});
  await page.screenshot({path:'.redesign-preview/wallpaper-mobile.png'});
  if (failures.length) throw new Error(JSON.stringify(failures));
  return {photos:photos.length, imageRequests:30, default:'Roni Supriyanto', fitAndPosition:'passed', viewports:[1440,768,390], exceptions:errors};
}
