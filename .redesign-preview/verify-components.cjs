async page => {
  const errors=[];page.on('pageerror',e=>errors.push(e.message));
  await page.goto('http://127.0.0.1:8091/.redesign-preview/');
  await page.evaluate(()=>{localStorage.removeItem('masjidku-preview-components');localStorage.removeItem('masjidku-preview-logo');});await page.reload();
  const selectors={logo:'.mosque-identity>svg',name:'#mosqueName',address:'#mosqueAddress',clock:'#clock',date:'.tv-clock>span',countdown:'.next-prayer',information:'#information',schedule:'.prayer-times',ticker:'.tv-footer'};
  for(const [id,selector] of Object.entries(selectors)) {
    await page.locator('#editComponent').selectOption(id);await page.locator('#componentVisible').uncheck();
    if(!await page.locator(selector).isHidden())throw Error('Cannot hide '+id);
    await page.locator('#componentVisible').check();if(!await page.locator(selector).isVisible())throw Error('Cannot restore '+id);
  }
  await page.locator('#editComponent').selectOption('countdown');
  await page.locator('#componentText').fill('Mari bersiap berjamaah');
  await page.locator('#componentBackground').fill('#123456');await page.locator('#componentColor').fill('#abcdef');
  await page.locator('#componentOpacity').fill('25');await page.locator('#componentSize').fill('120');
  if(await page.locator('.next-prayer h3').textContent()!=='Mari bersiap berjamaah')throw Error('Text missing');
  const style=await page.locator('.next-prayer').evaluate(n=>({bg:getComputedStyle(n).backgroundColor,color:getComputedStyle(n).color}));
  if(style.bg!=='rgba(18, 52, 86, 0.25)'||style.color!=='rgb(171, 205, 239)')throw Error(JSON.stringify(style));
  await page.locator('#saveComponents').click();await page.reload();
  if(await page.locator('.next-prayer h3').textContent()!=='Mari bersiap berjamaah')throw Error('Reload lost settings');
  await page.locator('[data-panel="identity"]').click();
  await page.locator('#logoUpload').setInputFiles({name:'logo.png',mimeType:'image/png',buffer:Buffer.from('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+jRZkAAAAASUVORK5CYII=','base64')});
  await page.locator('#uploadedLogo').waitFor();await page.reload();await page.locator('#uploadedLogo').waitFor();
  await page.locator('[data-panel="identity"]').click();await page.locator('#removeLogo').click();
  if(await page.locator('#uploadedLogo').count())throw Error('Logo removal failed');
  for(const width of [1440,768,390]){await page.setViewportSize({width,height:1100});if(!await page.evaluate(()=>document.documentElement.scrollWidth<=innerWidth))throw Error('Overflow '+width);}
  await page.locator('[data-panel="appearance"]').click();await page.setViewportSize({width:1440,height:1100});
  await page.locator('#screenEditor').screenshot({path:'C:/Users/T6N0CV02S35823C/Github/mosque/.redesign-preview/component-editor.png'});
  await page.evaluate(()=>{localStorage.removeItem('masjidku-preview-components');localStorage.removeItem('masjidku-preview-logo');});await page.reload();
  if(errors.length)throw Error(JSON.stringify(errors));
  return {components:9,visibility:'passed',styleAndPersistence:'passed',logo:'passed',viewports:[1440,768,390],errors};
}
