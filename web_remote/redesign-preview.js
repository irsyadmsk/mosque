import { animate } from 'framer-motion/dom';
import { createScreenEditor, editorStyles } from './screen-editor.js';

const $ = id => document.getElementById(id);
const preference = window.matchMedia('(prefers-reduced-motion: reduce)');
let direction = 'board';
let slide = 0;
let currentAnimation;
let dotAnimation;
const names = { board: 'Papan Jadwal', mihrab: 'Mihrab Tenang', grid: 'Grid Informasi' };
const reduceMotion = () => preference.matches || $('reduceMotion').checked;
let photoRequest = 0;
function resetPhotoSelection() {
  document.querySelectorAll('.photo-choice, .wallpaper-choice').forEach(button => {
    button.classList.remove('selected');
    button.setAttribute('aria-pressed', 'false');
  });
}
async function selectPhoto(photo, button) {
  const request = ++photoRequest;
  $('photoStatus').textContent = `Memuat foto ${photo.name}…`;
  try {
    const image = new Image();
    image.src = photo.url;
    await image.decode();
    if (request !== photoRequest) return;
    $('wallpaper').style.backgroundImage = `url('${photo.url}')`;
    $('wallpaper').style.backgroundPosition = `50% ${photo.position}%`;
    $('photoPosition').value = photo.position;
    $('positionValue').textContent = `${photo.position}%`;
    resetPhotoSelection();
    button.classList.add('selected');
    button.setAttribute('aria-pressed', 'true');
    $('photoStatus').textContent = `Foto aktif: ${photo.name}`;
  } catch {
    if (request === photoRequest) $('photoStatus').textContent = `Foto ${photo.name} tidak dapat dimuat. Pilih foto lain atau periksa berkas aslinya.`;
  }
}
async function loadPhotos() {
  try {
    const response = await fetch('/.redesign-preview/photos.json');
    if (!response.ok) throw new Error('Catalogue unavailable');
    const photos = await response.json();
    $('photoGallery').replaceChildren();
    for (const photo of photos) {
      const button = document.createElement('button');
      button.type = 'button';
      button.className = 'photo-choice';
      button.dataset.photo = photo.id;
      button.disabled = !photo.available;
      button.setAttribute('aria-pressed', 'false');
      const image = document.createElement('img');
      image.src = photo.thumbnail;
      image.alt = '';
      image.loading = 'lazy';
      image.decoding = 'async';
      const name = document.createElement('span');
      name.textContent = photo.name;
      button.append(image, name);
      button.addEventListener('click', () => selectPhoto(photo, button));
      $('photoGallery').append(button);
    }
    const initial = photos.find(photo => photo.available);
    if (initial) await selectPhoto(initial, $('photoGallery').querySelector(`[data-photo="${initial.id}"]`));
    else $('photoStatus').textContent = 'Foto asli tidak ditemukan. Pilih wallpaper bawaan di bawah.';
  } catch {
    $('photoGallery').textContent = 'Pilihan foto belum dapat dimuat. Muat ulang halaman atau jalankan server pratinjau.';
  }
}
loadPhotos();
$('photoFit').addEventListener('change', () => {
  $('wallpaper').style.backgroundSize = $('photoFit').value;
  $('photoPosition').disabled = $('photoFit').value === 'contain';
});
$('photoPosition').addEventListener('input', () => {
  $('wallpaper').style.backgroundPosition = `50% ${$('photoPosition').value}%`;
  $('positionValue').textContent = `${$('photoPosition').value}%`;
});
function reveal(element) {
  currentAnimation?.stop();
  if (!reduceMotion()) currentAnimation = animate(element, { opacity: [0.65, 1], y: [6, 0] }, { duration: 0.32, ease: 'easeOut' });
  else { element.style.opacity = '1'; element.style.transform = 'none'; }
}
document.querySelectorAll('.direction').forEach(button => button.addEventListener('click', () => {
  direction = button.dataset.direction;
  $('tv').dataset.direction = direction;
  $('layoutLabel').textContent = names[direction];
  $('reviewSummary').textContent = `Arah aktif: ${names[direction]}. Pengaturan ini tidak dikirim ke TV.`;
  document.querySelectorAll('.direction').forEach(other => {
    const selected = other === button;
    other.classList.toggle('selected', selected);
    other.setAttribute('aria-pressed', String(selected));
  });
}));
document.querySelectorAll('.wallpaper-choice').forEach(button => button.addEventListener('click', () => {
  ++photoRequest;
  resetPhotoSelection();
  $('wallpaper').style.backgroundImage = `url('/app/src/main/res/drawable/bg_preset_${button.dataset.wallpaper}.jpg')`;
  $('wallpaper').style.backgroundPosition = 'center';
  $('photoStatus').textContent = `Wallpaper aktif: ${button.textContent.trim()}`;
  document.querySelectorAll('.wallpaper-choice').forEach(other => {
    other.classList.toggle('selected', other === button);
    other.setAttribute('aria-pressed', String(other === button));
  });
}));
$('dim').addEventListener('input', () => {
  $('shade').style.opacity = Number($('dim').value) / 100;
  $('dimValue').textContent = `${$('dim').value}%`;
});
document.querySelectorAll('.nav-button').forEach(button => button.addEventListener('click', () => {
  document.querySelectorAll('.settings-panel:not(#worship-controls)').forEach(panel => panel.hidden = panel.id !== button.dataset.panel);
  document.querySelectorAll('.nav-button').forEach(other => {
    other.classList.toggle('active', other === button);
    if (other === button) other.setAttribute('aria-current', 'page'); else other.removeAttribute('aria-current');
  });
}));
for (const [input, target] of [['name', 'mosqueName'], ['address', 'mosqueAddress'], ['announcementInput', 'announcement']]) {
  $(input).addEventListener('input', () => $(target).textContent = $(input).value.trim() || (input === 'name' ? 'Nama masjid' : ''));
}
$('showInformation').addEventListener('change', () => $('information').hidden = !$('showInformation').checked);
function changeSlide() {
  if (activeComponents?.information.text) return;
  slide = 1 - slide;
  $('information').querySelector('h4').textContent = slide ? 'Laporan kas masjid' : 'Kajian selepas Maghrib';
  $('information').querySelector('p').textContent = slide ? 'Saldo contoh Rp 24.750.000. Laporan lengkap tersedia melalui pengurus.' : 'Membangun kebiasaan baik, dimulai dari keluarga.';
  $('slideStatus').textContent = slide ? 'Laporan kas' : 'Jadwal kajian';
  $('information').querySelector('.info-top span').textContent = `${slide + 1} / 2`;
  $('information').querySelector('.info-detail').textContent = slide ? 'Data simulasi untuk menilai desain' : 'Ruang utama masjid · Terbuka untuk jamaah';
  reveal($('information'));
}
$('next').addEventListener('click', changeSlide);
$('previous').addEventListener('click', changeSlide);
$('replay').addEventListener('click', () => {
  dotAnimation?.stop();
  if (!reduceMotion()) dotAnimation = animate($('demoDot'), { x: [0, 55, 0], opacity: [0.65, 1, 1] }, { duration: 0.64, ease: 'easeOut' });
  changeSlide();
});
function stopMotion() {
  if (reduceMotion()) {
    currentAnimation?.stop(); dotAnimation?.stop();
    $('information').style.opacity = '1'; $('information').style.transform = 'none';
    $('demoDot').style.opacity = '1'; $('demoDot').style.transform = 'none';
  }
}
$('reduceMotion').addEventListener('change', stopMotion);
preference.addEventListener('change', stopMotion);
document.addEventListener('visibilitychange', () => { if (document.hidden) {currentAnimation?.stop();dotAnimation?.stop();} });
$('fullscreen').addEventListener('click', async () => {
  try { await $('tv').requestFullscreen(); } catch { $('reviewSummary').textContent = 'Browser tidak mengizinkan layar penuh. Gunakan pratinjau di halaman ini.'; }
});
$('reset').addEventListener('click', () => {
  localStorage.removeItem('masjidku-preview-components');localStorage.removeItem('masjidku-preview-logo');window.location.reload();
});

let worshipMode = 'normal';
const worshipModes = {
  normal: ['Informasi', ''],
  adhan: ['Waktu adzan', 'Mari bersiap untuk salat berjamaah'],
  khutbah: ['Khutbah Jumat berlangsung', 'Mohon tenang dan dengarkan khutbah'],
  iqomah: ['Menuju iqomah · 05:00', 'Luruskan dan rapatkan saf'],
  prayer: ['Salat berjamaah berlangsung', 'Mohon senyapkan ponsel dan jaga ketenangan']
};
function renderWorship() {
  const focus = worshipMode === 'khutbah' || worshipMode === 'prayer';
  const blank = focus && $('focusBlank').checked;
  $('focusPreview').hidden = worshipMode === 'normal';
  $('focusTitle').textContent = blank ? '' : worshipModes[worshipMode][0];
  $('focusMessage').textContent = blank ? '' : worshipModes[worshipMode][1];
  $('focusClock').hidden = blank || !$('focusClockEnabled').checked;
  $('worshipStatus').textContent = `Mode contoh: ${worshipModes[worshipMode][0]}`;
  document.querySelectorAll('[data-worship]').forEach(button => button.setAttribute('aria-pressed', String(button.dataset.worship === worshipMode)));
  currentAnimation?.stop(); dotAnimation?.stop();
}
document.querySelectorAll('[data-worship]').forEach(button => button.addEventListener('click', () => { worshipMode = button.dataset.worship; renderWorship(); }));
$('focusBlank').addEventListener('change', renderWorship);
$('focusClockEnabled').addEventListener('change', renderWorship);
renderWorship();

const customStyle = document.createElement('style');customStyle.textContent=editorStyles;document.head.append(customStyle);
const componentElements = {
  logo:document.querySelector('.mosque-identity>svg'),name:$('mosqueName'),address:$('mosqueAddress'),
  clock:$('clock'),date:document.querySelector('.tv-clock>span'),countdown:document.querySelector('.next-prayer'),
  information:$('information'),schedule:document.querySelector('.prayer-times'),ticker:document.querySelector('.tv-footer')
};
const textTargets = {name:$('mosqueName'),address:$('mosqueAddress'),countdown:document.querySelector('.next-prayer h3'),information:document.querySelector('.information h4'),ticker:$('announcement')};
const originalTexts = Object.fromEntries(Object.entries(textTargets).map(([key,node])=>[key,node.textContent]));
const originalSizes = new WeakMap();
let activeComponents;
function applyComponents(settings) {
  activeComponents=settings;
  for (const [id,element] of Object.entries(componentElements)) {
    const c=settings[id];element.toggleAttribute('hidden',!c.visible);
    const rgb=c.background.match(/\w\w/g).map(v=>parseInt(v,16));
    element.style.setProperty('background',`rgba(${rgb.join(',')},${c.opacity})`,'important');
    for (const node of [element,...element.querySelectorAll('h2,h3,h4,p,strong,span,small,b')]) {
      if (!originalSizes.has(node)) originalSizes.set(node,parseFloat(getComputedStyle(node).fontSize)/$('tv').getBoundingClientRect().width*100);
      node.style.setProperty('color',c.color,'important');
      node.style.fontSize=`${originalSizes.get(node)*c.size}cqw`;
    }
    if (textTargets[id]) {
      const input={name:'name',address:'address',ticker:'announcementInput'}[id];
      textTargets[id].textContent=c.text || (input?$(input).value:originalTexts[id]);
    }
    if (id==='information') {
      element.style.setProperty('display',c.visible?'block':'none','important');
      for (const child of element.querySelectorAll('.info-top,p,.info-detail,.slide-controls')) child.hidden=Boolean(c.text);
    }
    if (id==='schedule') for (const cell of element.children) cell.style.setProperty('background','transparent','important');
  }
  $('showInformation').checked=settings.information.visible;
}
const componentEditor=createScreenEditor($('screenEditor'), {preview:true,onChange:applyComponents,save:settings=>{
  localStorage.setItem('masjidku-preview-components',JSON.stringify(settings));
}});
let savedComponents='';try{savedComponents=localStorage.getItem('masjidku-preview-components')||'';}catch{}
componentEditor.load(savedComponents);
$('showInformation').addEventListener('change',()=>{
  activeComponents.information.visible=$('showInformation').checked;
  $('componentStatus').textContent='Perubahan belum disimpan.';
});
const defaultLogo=componentElements.logo;
let logoUrl;
function showLogo(data) {
  if(logoUrl)URL.revokeObjectURL(logoUrl);
  const previous=document.querySelector('#uploadedLogo');if(previous)previous.remove();
  if (!data) { componentElements.logo=defaultLogo;defaultLogo.style.display=''; }
  else {
    const image=document.createElement('img');image.id='uploadedLogo';image.alt='Logo masjid';image.src=data;
    image.style.cssText='width:54px;height:54px;object-fit:contain;flex-shrink:0';defaultLogo.after(image);defaultLogo.style.display='none';componentElements.logo=image;
  }
  applyComponents(activeComponents);
}
try{const logo=localStorage.getItem('masjidku-preview-logo');if(logo)showLogo(logo);}catch{}
$('logoUpload').addEventListener('change',async()=>{
  try {
    const file=$('logoUpload').files[0];if(!file)return;
    if(!['image/png','image/jpeg','image/webp'].includes(file.type)||file.size>1024*1024)throw Error('Pilih PNG, JPEG atau WebP maksimal 1 MB.');
    const data=await new Promise((resolve,reject)=>{const reader=new FileReader();reader.onload=()=>resolve(reader.result);reader.onerror=reject;reader.readAsDataURL(file);});
    const image=new Image();image.src=data;await image.decode();
    localStorage.setItem('masjidku-preview-logo',data);showLogo(data);$('logoStatus').textContent='Logo tersimpan di browser ini. Unggah terpisah melalui admin TV untuk menerapkan.';
  } catch(e) { $('logoStatus').textContent=e.message || 'Logo tidak dapat dibaca.'; }
  finally { $('logoUpload').value=''; }
});
$('removeLogo').addEventListener('click',()=>{localStorage.removeItem('masjidku-preview-logo');showLogo('');$('logoStatus').textContent='Logo bawaan dipakai.';});
