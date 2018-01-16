library('org.renjin.cran:randomForest')
"la librairie randomForest est chargée"
iris <- read.csv("src/iris.csv")
"les données des iris sont chargées"
arbre <- randomForest(Species~Sepal.Length+Sepal.Width+Petal.Length+Petal.Width,data=iris)
total <- length(iris$Species)
conf <- arbre$confusion[,1:3]
accuracy <- (conf[1,1] + conf[2,2] + conf[3,3])/total