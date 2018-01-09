library("org.renjin.cran.tree")
iris <- read.csv("src/iris.csv")
arbre <- tree(Species~Sepal.Length+Sepal.Width+Petal.Length+Petal.Width,data=iris)
3+3