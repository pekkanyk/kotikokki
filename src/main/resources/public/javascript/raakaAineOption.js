function uusiRivi(){
    
    var raakaAine = document.createElement("input");
        raakaAine.type = "hidden";
        raakaAine.name = "raakaAine";
        var raakaAineArvo = document.getElementById("raakaAine").value;
        raakaAine.value = raakaAineArvo;
    document.getElementById("ainekset").appendChild(raakaAine);
    
    var maara = document.createElement("input");
        maara.type = "hidden";
        maara.name = "maara";
        var maaraArvo = document.getElementById("maara").value;
        maara.value = maaraArvo;
    document.getElementById("ainekset").appendChild(maara);
    
    var yksikko = document.createElement("input");
        yksikko.type = "hidden";
        yksikko.name = "yksikko";
        var yksikkoArvo = document.getElementById("yksikko").value;
        yksikko.value = yksikkoArvo;
    document.getElementById("ainekset").appendChild(yksikko);
    
    var row = document.createElement("div");
        row.class = "row";
    var col = document.createElement("div");
        col.class = "col-xs-12 col-12";
    var text = document.createTextNode(maaraArvo+" "+yksikkoArvo+" "+raakaAineArvo);
    col.appendChild(text);
    row.appendChild(col);
    document.getElementById("ainekset").appendChild(row);
    document.getElementById("maara").value = null;
    document.getElementById("raakaAine").value = null;
    document.getElementById("yksikko").value = null;
    document.getElementById("raakaAine").focus();
}