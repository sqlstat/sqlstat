export TESTDIR=/tmp/docs
rm -rf $TESTDIR
mkdir -p $TESTDIR

mkdir -p $TESTDIR/p1/src
mkdir -p $TESTDIR/p2/src
mkdir -p $TESTDIR/p3/src
mkdir -p $TESTDIR/p4/src
echo -e "select \n * \n From test" > $TESTDIR/p1/src/1.sh
echo -e "update test \n set name='fan'\n" > $TESTDIR/p1/src/2.sh
echo -e "delete from test where name='fan'\n" > $TESTDIR/p1/src/3.sh
echo -e "insert into test values('fan')\n" > $TESTDIR/p1/src/4.sh
echo -e "sqlldr asdf  asdfeed" > $TESTDIR/p1/src/5.sh
echo -e "select update" > $TESTDIR/p1/src/6.sh

echo -e "sqluldr asdf\n  asdfeed" > $TESTDIR/p2/src/1.sh
echo -e "sqlplus ade add " > $TESTDIR/p2/src/2.sh
echo -e "LOAD data \n infile okh" > $TESTDIR/p2/src/3.ctl
echo -e "select \n * \n From test" > $TESTDIR/p2/src/4.sql
echo -e "select \n * \n From test" > $TESTDIR/p2/src/5.sql
echo -e "delete update" > $TESTDIR/p2/src/6.sh

echo -e "sqluldr asdf\n  asdfeed" > $TESTDIR/p3/src/1.qry
echo -e "sqlplus ade add " > $TESTDIR/p3/src/2.qry1
echo -e "LOAD data \n infile okh" > $TESTDIR/p3/src/3.ctl
echo -e " dda \n dbms_metadata.put \n adfe" > $TESTDIR/p3/src/4.sql
echo -e " dda \ndbms_output.put \n adfe" > $TESTDIR/p3/src/5.sql
echo -e "delete update" > $TESTDIR/p3/src/6.qry

cp ibatisTestFile/UserPo.xml  $TESTDIR/p4/src/
cp ibatisTestFile/UserPo1.xml  $TESTDIR/p4/src/
cp ibatisTestFile/UserPo2.xml  $TESTDIR/p4/src/
cp ibatisTestFile/UserPo3.xml  $TESTDIR/p4/src/
cp ibatisTestFile/UserPo4.xml  $TESTDIR/p4/src/
cp ibatisTestFile/UserPo5.xml  $TESTDIR/p4/src/
cp ibatisTestFile/UserPo6.xml  $TESTDIR/p4/src/