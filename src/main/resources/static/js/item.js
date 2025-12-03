(function(){
    'use strict';

    const $ = (id)=>document.getElementById(id);
    const body = document.body;
    const role = (body.getAttribute('data-role')||'STORE').toUpperCase();
    const itemNo = body.getAttribute('data-item-no');

    function toast(msg,type){
        let box=document.getElementById('toastBox');
        if(!box){ box=document.createElement('div'); box.id='toastBox';
            box.style.cssText='position:fixed;right:16px;bottom:16px;z-index:9999'; document.body.appendChild(box); }
        const el=document.createElement('div');
        el.className='alert alert-'+(type||'success')+' shadow-sm py-2 px-3 mb-2';
        el.textContent=msg; box.appendChild(el);
        setTimeout(()=>{ el.style.transition='opacity .2s'; el.style.opacity='0'; setTimeout(()=>el.remove(),200); },1800);
    }
    function setText(id, v){ const el=$(id); if(el) el.textContent = (v ?? '-'); }

    // 관리자만 버튼(서버 th:if에도 걸어두었지만 클라이언트 보정)
    (function toggleAdminButtons(){
        const admin = (role==='MANAGER');
        ['btnEdit','btnDelete'].forEach(id=>{ const el=$(id); if(el) el.style.display = admin? '' : 'none'; });
    })();

    if(!itemNo) return;

    fetch('/api/items/'+itemNo)
        .then(res=>{ if(!res.ok) throw new Error(); return res.json(); })
        .then(item=>{
            setText('d-category', item.itemCategory);
            setText('d-itemCode', item.itemCode);
            setText('d-itemName', item.itemName);
            setText('d-ingredientName', item.ingredientName);

            setText('d-stockUnit', item.stockUnit);
            setText('d-supplyUnit', item.supplyUnit);

            const conv = (item.convertStock!=null)? item.convertStock.toLocaleString() : null;
            setText('d-convert', conv? `${conv} ${item.stockUnit||''}`.trim() : '-');

            setText('d-itemPrice', item.itemPrice!=null? `${item.itemPrice.toLocaleString()} 원` : '-');
            setText('d-supplier', item.supplier);

            setText('d-storageType', item.storageType);
            setText('d-expType', item.expirationType);
            setText('d-expiration', item.expiration!=null? `${item.expiration} 일` : '-');

            setText('d-note', item.note);
            const img = $('d-itemImage');
            if(img) img.src = item.itemImage || 'https://via.placeholder.com/220x160';
            if(img) img.onerror = ()=>{ img.src='https://via.placeholder.com/220x160'; };
        })
        .catch(()=> toast('품목 정보를 불러오지 못했습니다.','danger'));

    // 액션
    const btnEdit = $('btnEdit');
    if(btnEdit) btnEdit.addEventListener('click', ()=> location.href='/item/set?itemNo='+itemNo);

    const btnDelete = $('btnDelete');
    if(btnDelete) btnDelete.addEventListener('click', ()=>{
        if(role!=='MANAGER') return;
        if(!confirm('정말 삭제(소프트 삭제)하시겠습니까?')) return;

        const t=document.querySelector('meta[name="_csrf"]')?.content;
        const h=document.querySelector('meta[name="_csrf_header"]')?.content;
        const headers={'Content-Type':'application/json'};
        if(t&&h) headers[h]=t;

        fetch('/api/items/'+itemNo,{method:'DELETE', headers, credentials:'same-origin'})
            .then(res=>{ if(!res.ok) throw new Error(); toast('삭제되었습니다.','success'); setTimeout(()=>location.href='/item/get',400); })
            .catch(()=> toast('삭제에 실패했습니다.','danger'));
    });

    const back = $('btnBackList');
    if(back) back.addEventListener('click', ()=> location.href='/item/get');

})();
