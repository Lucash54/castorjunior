<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr-FR">
<head>
<title>Results</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
body {
background-color: grey;
background-image: url("castors2.png");
background-repeat: no-repeat;
background-position:center top;
}
#conteneur { overflow: hidden; }
#conteneur div { margin:5px;width:30%;float:left; }
</style>
</head>
 
<body>
 
  <div>
<h1>Results : </h1>
  </div>
 
  <div id="conteneur">
    <div>
Algo 1 :
<pre></pre>
${lib1}
<pre></pre>
${method1}
<pre></pre>
${pct1}% de l'echantillon est l'apprentissage
<pre></pre>
${moy1} (moyenne)
<pre></pre>
${tree1} arbres (si random forest)
<pre></pre>
Accuracy = ${res1}
<img src="<%=request.getContextPath()%>${image1}" alt="">
    </div>
    <div>
Algo 2 :
<pre></pre>
${lib2}
<pre></pre>
${method2}
<pre></pre>
${pct2}% de l'echantillon est l'apprentissage
<pre></pre>
${moy2} (moyenne)
<pre></pre>
${tree2} arbres (si random forest)
<pre></pre>
Accuracy = ${res2}
<img src="<%=request.getContextPath()%>${image2}" alt="">
    </div>
    <div>
Algo 3 :
<pre></pre>
${lib3}
<pre></pre>
${method3}
<pre></pre>
${pct3}% de l'echantillon est l'apprentissage
<pre></pre>
${moy3} (moyenne)
<pre></pre>
${tree3} arbres (si random forest)
<pre></pre>
Accuracy = ${res3}
<img src="<%=request.getContextPath()%>${image3}" alt="">
    </div>
  </div>

 
</body>
</html>