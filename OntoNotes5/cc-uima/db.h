#ifndef DB_H_
#define DB_H_

#include <mysql.h>


namespace ontonotes5 {

  void db_die(const char *msg, MYSQL *mysql);
  void db_connect(MYSQL *mysql);
  void db_disconnect(MYSQL *mysql);

  void db_query(MYSQL *mysql, const char *query);

}

#endif  // DB_H_
