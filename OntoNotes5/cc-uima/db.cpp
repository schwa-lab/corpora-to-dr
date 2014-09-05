#include "db.h"

#include <iostream>

#include <my_global.h>
#include <my_sys.h>


namespace ontonotes5 {

void
db_die(const char *msg, MYSQL *const mysql) {
  std::cerr << msg << ": " << mysql_error(mysql) << std::endl;
  std::exit(1);
}


void
db_connect(MYSQL *const mysql) {
  if (mysql_options(mysql, MYSQL_SET_CHARSET_NAME, "utf8") != 0)
    db_die("Failed to set charset", mysql);
  if (mysql_real_connect(mysql, "129.78.xxx.xx", "ontonotes", "xxxxxxxxx", "ontonotes5", 0, nullptr, 0) != mysql)
    db_die("Failed to connect to the DB", mysql);
}


void
db_disconnect(MYSQL *const mysql) {
  mysql_close(mysql);
}


void
db_query(MYSQL *const mysql, const char *const query) {
  mysql_thread_end();
  if (mysql_query(mysql, query) != 0)
    db_die("Failed to call mysql_query", mysql);
}

}  // namespace ontonotes5
