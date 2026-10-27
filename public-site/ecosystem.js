// Uses the versioned Irsyads preference runtime. No identity/session data.
(() => {
 const root=document.documentElement;
 const navigation=document.createElement('div');document.querySelector('header nav')?.append(navigation);window.IrsyadsServiceNavigation?.mount(navigation,'masjidku-tv');
 const theme=document.getElementById("ecosystem-theme");
 const palette=document.getElementById("ecosystem-palette");
 const read=(key,fallback)=>{try{return localStorage.getItem(key)||fallback}catch{return fallback}};
 const sync=()=>{theme.value=read("irsyads-theme","system");palette.value=root.dataset.palette||"krem"};
 sync();
 theme.addEventListener("change",()=>window.dispatchEvent(new CustomEvent("irsyads-appearance-change",{detail:{theme:theme.value}})));
 palette.addEventListener("change",()=>window.dispatchEvent(new CustomEvent("irsyads-appearance-change",{detail:{palette:palette.value}})));
 window.addEventListener("focus",sync);
 window.addEventListener("pageshow",sync);
 window.addEventListener('irsyads-preferences-applied',sync);
 const language=IrsyadsLanguage.preferredLanguage()||"en";
 const apps=document.getElementById("ecosystem-apps");
 apps.href="https://irsyads.com"+(language==="en"?"":"/"+language)+"/apps";
 document.addEventListener("keydown",event=>{if(event.key==="Escape"){document.querySelector(".appearance-controls").open=false}});
})();
