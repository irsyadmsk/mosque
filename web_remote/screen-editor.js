// Shared controls for local design preview and the embedded Android LAN admin.
function createScreenEditor(host, options) {
  const labels = {logo:'Logo',name:'Nama masjid',address:'Alamat',clock:'Jam',date:'Tanggal',countdown:'Hitung mundur',information:'Informasi & media',schedule:'Jadwal salat',ticker:'Maklumat'};
  const editable = new Set(['name','address','countdown','information','ticker']);
  let settings = {}; let dirty = false;
  const defaults = () => ({visible:true,background:'#102B24',color:'#F5F7F4',opacity:0.75,size:1,text:''});
  host.innerHTML = `<h2>Komponen layar</h2><p>Atur setiap komponen. Transparansi hanya memengaruhi latar panel, sehingga teks tetap jelas.</p>
    <label for="editComponent">Komponen</label><select id="editComponent"></select>
    <label class="check-row"><input id="componentVisible" type="checkbox"> Tampilkan komponen ini</label>
    <div class="component-fields"><label>Warna latar<input id="componentBackground" type="color"></label><label>Warna teks<input id="componentColor" type="color"></label>
    <label>Kepekatan latar <output id="componentOpacityValue"></output><input id="componentOpacity" type="range" min="0" max="100"></label>
    <label>Ukuran <output id="componentSizeValue"></output><input id="componentSize" type="range" min="70" max="140"></label></div>
    <label for="componentText">Teks pengganti (kosongkan untuk memakai data masjid)</label><textarea id="componentText" rows="3" maxlength="500"></textarea>
    <p id="componentHint" class="hint"></p><div class="component-actions"><button type="button" id="saveComponents">${options.preview ? 'Simpan di browser ini' : 'Simpan & terapkan di TV'}</button>
    <button type="button" id="restoreComponent">Pulihkan komponen ini</button><button type="button" id="exportComponents">Unduh pengaturan</button>
    <label>Impor pengaturan<input id="importComponents" type="file" accept="application/json,.json"></label></div><p id="componentStatus" role="status"></p>`;
  const $ = id => host.querySelector('#'+id);
  for (const [id,label] of Object.entries(labels)) { const item=document.createElement('option');item.value=id;item.textContent=label;$('editComponent').append(item); }
  function normal(raw) {
    return Object.fromEntries(Object.keys(labels).map(id => {
      const c = raw?.[id] || {}, d=defaults();
      for (const key of ['background','color']) if (/^#[0-9a-f]{6}$/i.test(c[key])) d[key]=c[key];
      if (typeof c.visible==='boolean') d.visible=c.visible;
      for (const [key,min,max] of [['opacity',0,1],['size',0.7,1.4]]) if (Number.isFinite(c[key])) d[key]=Math.max(min,Math.min(max,c[key]));
      if (editable.has(id) && typeof c.text==='string') d.text=c.text.slice(0,500);
      return [id,d];
    }));
  }
  function render() {
    const id=$('editComponent').value, c=settings[id];
    $('componentVisible').checked=c.visible;$('componentBackground').value=c.background;$('componentColor').value=c.color;
    $('componentOpacity').value=Math.round(c.opacity*100);$('componentOpacityValue').textContent=`${Math.round(c.opacity*100)}%`;
    $('componentSize').value=Math.round(c.size*100);$('componentSizeValue').textContent=`${Math.round(c.size*100)}%`;
    $('componentText').value=c.text;$('componentText').disabled=!editable.has(id);
    $('componentHint').textContent=id==='logo' ? (options.preview ? 'Unggah logo melalui Identitas masjid. Logo pratinjau tersimpan terpisah dari berkas pengaturan.' : 'Unggah atau hapus logo melalui tab Media → Logo Masjid.') : id==='information' ? 'Teks kosong menampilkan carousel. Warna isi kas, petugas, hadits dan media tetap memakai pengaturan tiap konten.' : !editable.has(id) ? 'Jam, tanggal dan jadwal memakai data otomatis. Ubah lokasi dan koreksi melalui pengaturan waktu.' : 'Teks pengganti hanya untuk komponen ini; data asal tetap tersimpan.';
    options.onChange?.(settings);
  }
  function update() {
    const id=$('editComponent').value;
    settings[id]={visible:$('componentVisible').checked,background:$('componentBackground').value,color:$('componentColor').value,opacity:Number($('componentOpacity').value)/100,size:Number($('componentSize').value)/100,text:$('componentText').value};
    dirty=true;render();$('componentStatus').textContent='Perubahan belum disimpan.';
  }
  for (const id of ['componentVisible','componentBackground','componentColor','componentOpacity','componentSize','componentText']) $(id).addEventListener('input',update);
  $('editComponent').addEventListener('change',render);
  $('restoreComponent').addEventListener('click',()=>{settings[$('editComponent').value]=defaults();dirty=true;render();$('componentStatus').textContent='Komponen dipulihkan. Simpan untuk menerapkan.';});
  $('saveComponents').addEventListener('click',async()=>{
    $('saveComponents').disabled=true;
    try { await options.save(settings);dirty=false;$('componentStatus').textContent=options.preview ? 'Tersimpan di browser ini. Belum dikirim ke TV.' : 'Tersimpan. Papan Jadwal Editable aktif di TV.'; }
    catch(e) { $('componentStatus').textContent='Gagal menyimpan: '+e.message; }
    finally { $('saveComponents').disabled=false; }
  });
  $('exportComponents').addEventListener('click',()=>{const url=URL.createObjectURL(new Blob([JSON.stringify(settings,null,2)],{type:'application/json'}));const a=document.createElement('a');a.href=url;a.download='masjidku-komponen.json';a.click();setTimeout(()=>URL.revokeObjectURL(url),1000);});
  $('importComponents').addEventListener('change',async()=>{try{const file=$('importComponents').files[0];if(!file)return;if(file.size>20000)throw Error('Berkas maksimal 20 KB');const raw=JSON.parse(await file.text());if(!raw||Array.isArray(raw)||typeof raw!=='object')throw Error('Format pengaturan tidak valid');settings=normal(raw);dirty=true;render();$('componentStatus').textContent='Pengaturan diimpor. Periksa lalu simpan.';}catch(e){$('componentStatus').textContent=e.message;}finally{$('importComponents').value='';}});
  return {load(json){if(dirty)return;try{settings=normal(JSON.parse(json||'{}'));}catch{settings=normal({});}render();}};
}

const editorStyles = `.component-editor{margin:20px 0;padding:20px;border:1px solid #ccdcd3;border-radius:12px;background:#fff;color:#17392c;min-width:0}.component-editor h2{margin:0 0 8px}.component-editor p{margin:8px 0;font-size:14px}.component-editor>label{display:block;margin-top:14px}.component-editor select,.component-editor textarea{width:100%;padding:10px;border:1px solid #b7cbbf;border-radius:6px;font:inherit}.component-fields{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:16px;margin:16px 0}.component-fields label{display:grid;gap:6px;min-width:0}.component-fields input{width:100%;min-height:44px}.component-actions{display:flex;flex-wrap:wrap;gap:10px;margin-top:16px}.component-actions button{min-height:44px;padding:10px;border:1px solid #b7cbbf;background:#eff6f1;color:#17392c;border-radius:6px;font:inherit}.component-actions input{max-width:100%;display:block}.component-editor :focus-visible{outline:3px solid #248166;outline-offset:3px}.component-editor .check-row{display:flex;align-items:center;gap:8px;min-height:44px}`;
export { createScreenEditor, editorStyles };
