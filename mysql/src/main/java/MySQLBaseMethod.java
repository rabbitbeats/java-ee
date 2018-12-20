/**
 * @author liucw
 * @version 1.0
 * @date 2017/12/16
 */
public class MySQLBaseMethod {
    // MYSQL中UNIX时间戳与日期的转换
    String sql = "" +
            "select FROM_UNIXTIME(1464973385.641,'%Y-%m-%d %H:%i:%s')" +
            "select UNIX_TIMESTAMP('2016-06-04 01:03:05');";
}
