pwd
cd ..
mvn package
cd docker || exit
docker build -t mikolka9144/worldcraft -f Dockerfile ./../
#confif segment
cd proxy || exit
sh start.sh