cd /tmp
rm -rf /tmp/docs
mkdir /tmp/docs
cd /tmp/docs
mkdir p1
mkdir p2
mkdir p3
mkdir p1/src
mkdir p2/src
mkdir p3/src
mkdir p1/target
mkdir p2/target
mkdir p3/target
echo -e "select \n * \n From test" > p1/src/1.sh
echo -e "update test \n set name='fan'\n" > p1/src/2.sh
echo -e "delete from test where name='fan'\n" > p1/src/3.sh
echo -e "insert into test values('fan')\n" > p1/src/4.sh
echo -e "sqlldr asdf  asdfeed" > p1/src/5.sh
echo -e "select update" > p1/src/6.sh

echo -e "sqluldr asdf\n  asdfeed" > p2/src/1.sh
echo -e "sqlplus ade add " > p2/src/2.sh
echo -e "LOAD data \n infile okh" > p2/src/3.ctl
echo -e "select \n * \n From test" > p2/src/4.sql
echo -e "select \n * \n From test" > p2/src/5.sql
echo -e "delete update" > p2/src/6.sh

echo -e "sqluldr asdf\n  asdfeed" > p3/src/1.qry
echo -e "sqlplus ade add " > p3/src/2.qry1
echo -e "LOAD data \n infile okh" > p3/src/3.ctl
echo -e " dda \n dbms_metadata.put \n adfe" > p3/src/4.sql
echo -e " dda \ndbms_output.put \n adfe" > p3/src/5.sql
echo -e "delete update" > p3/src/6.qry