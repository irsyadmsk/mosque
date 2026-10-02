async page => {
  const errors=[],saves=[];
  const fixture=await page.request.get('http://127.0.0.1:8092/admin-review-status.json').then(r=>r.json());
  page.on('pageerror',e=>errors.push(e.message));
  await page.route('**/api/**',async route=>{
    const path=new URL(route.request().url()).pathname;
    if(path==='/api/save-config')saves.push(route.request().postDataJSON());
    await route.fulfill({contentType:'application/json',body:JSON.stringify(path==='/api/status'?fixture:{status:'ok',authenticated:true})});
  });
  await page.goto('http://127.0.0.1:8092/admin-review.html');
  await page.evaluate(data=>{localStorage.setItem('masjidku_session_token','review-only');showDashboard();renderData(data);switchTab('tab-kontrol');},fixture);
  await page.locator('#editComponent').selectOption('clock');
  if(await page.locator('#componentVisible').isChecked())throw Error('Saved clock visibility not loaded');
  await page.locator('#componentVisible').check();await page.locator('#componentBackground').fill('#123456');
  await page.locator('#componentOpacity').fill('35');await page.locator('#saveComponents').click();
  await page.waitForFunction(()=>document.getElementById('componentStatus').textContent.includes('Tersimpan'));
  const payload=saves.at(-1),parts=JSON.parse(payload.screenCustomizationJson);
  if(payload.displayLayoutModel!=='CUSTOMIZABLE_BOARD'||parts.clock.visible!==true||parts.clock.opacity!==0.35||parts.clock.background!=='#123456')throw Error('Save contract mismatch');
  for(const width of [1440,390]){await page.setViewportSize({width,height:1100});if(!await page.evaluate(()=>document.documentElement.scrollWidth<=innerWidth))throw Error('Overflow '+width);}
  if(errors.length)throw Error(JSON.stringify(errors));
  await page.locator('#screenEditor').screenshot({path:'C:/Users/T6N0CV02S35823C/Github/mosque/.redesign-preview/admin-component-editor.png'});
  return {embeddedAdmin:'passed',hydration:'passed',saveContract:'passed',viewports:[1440,390],errors};
}
