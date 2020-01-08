function editNimi(){
    
    document.getElementById("nimi").removeAttribute("readonly");
    document.getElementById("nimi").setAttribute("class", "inputbox-pitka");
    document.getElementById("niminappi").setAttribute("onclick", "saveNimi()");
    document.getElementById("niminappi").setAttribute("value", "SAVE");
}

function saveNimi(){
    
    document.getElementById("nimi").setAttribute("readonly", "readonly");
    document.getElementById("nimi").setAttribute("class", "noBorder inputbox-pitka");
    document.getElementById("niminappi").setAttribute("onclick", "editNimi()");
    document.getElementById("niminappi").setAttribute("value", "EDIT");
}

function poistaRaakaAine(id){
    document.getElementById(id).remove();
            
}

function uusiRivi(){
    
    var raakaAine = document.createElement("input");
        raakaAine.type = "hidden";
        raakaAine.name = "raakaAine";
        raakaAine.value = document.getElementById("raakaAine").value;
    
    var maara = document.createElement("input");
        maara.type = "hidden";
        maara.name = "maara";
        maara.value = document.getElementById("maara").value;
    
    var yksikko = document.createElement("input");
        yksikko.type = "hidden";
        yksikko.name = "yksikko";
        yksikko.value = document.getElementById("yksikko").value;
         
    var nappi = document.createElement("input");
        nappi.type = "button";
        nappi.value = "del";
        var onClick = "poistaRaakaAine('"+document.getElementById("raakaAine").value+"')";
        nappi.setAttribute("onclick", onClick);
        
    
    
    
    var row = document.createElement("tr");
        row.id = raakaAine.value;
    var col1 = document.createElement("td");
        col1.appendChild(document.createTextNode(raakaAine.value));
    var col2 = document.createElement("td");
        col2.appendChild(document.createTextNode(maara.value));
    var col3 = document.createElement("td");
        col3.appendChild(document.createTextNode(yksikko.value));
    var col4 = document.createElement("td");
    
    col4.appendChild(nappi);
    col4.appendChild(raakaAine);
    col4.appendChild(maara);
    col4.appendChild(yksikko);
    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    
    document.getElementById("ainekset").appendChild(row);
    document.getElementById("maara").value = null;
    document.getElementById("raakaAine").value = null;
    document.getElementById("yksikko").value = null;
    document.getElementById("raakaAine").focus();
}


