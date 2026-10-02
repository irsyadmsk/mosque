// Uses the versioned Irsyads preference runtime. No identity/session data.
(() => {
 const root=document.documentElement;
 const theme=document.getElementById("ecosystem-theme");
 const palette=document.getElementById("ecosystem-palette");
 const read=(key,fallback)=>{try{return localStorage.getItem(key)||fallback}catch{return fallback}};
 const sync=()=>{theme.value=read("irsyads-theme","system");palette.value=root.dataset.palette||"krem"};
 sync();
 theme.addEventListener("change",()=>window.dispatchEvent(new CustomEvent("irsyads-appearance-change",{detail:{theme:theme.value}})));
 palette.addEventListener("change",()=>window.dispatchEvent(new CustomEvent("irsyads-appearance-change",{detail:{palette:palette.value}})));
 window.addEventListener("focus",sync);
 window.addEventListener("pageshow",sync);
 const language=IrsyadsLanguage.preferredLanguage()||"id";
 const apps=document.getElementById("ecosystem-apps");
 apps.href="https://irsyads.com"+(language==="id"?"":"/"+language)+"/apps";
 document.addEventListener("keydown",event=>{if(event.key==="Escape"){document.querySelector(".appearance-controls").open=false}});
})();
