function editoiKategoriaa(id){
    var toiminto = "toiminto"+id;
    var kuva = "kuva"+id;
    document.getElementById(toiminto).removeAttribute("onclick");
    
    document.getElementById(kuva).setAttribute("title", "tallenna");
    document.getElementById(kuva).setAttribute("src", "/img/ico_write.gif");
}