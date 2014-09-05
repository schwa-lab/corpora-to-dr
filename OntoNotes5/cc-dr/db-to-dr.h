#ifndef DB_TO_DR_H_
#define DB_TO_DR_H_

#include <string>
#include <unordered_map>

#include "models-db.h"
#include "models-dr.h"


namespace ontonotes5 {
  namespace to_dr {

    class Converter {
    private:
      Doc *doc;
      const ODocument *odoc;
      std::unordered_map<const OParseNode *, ParseNode *> onode_to_node;
      std::unordered_map<const OParseNode *, Token *> onode_to_token;
      std::unordered_map<const OSpeaker *, Speaker *> ospeaker_to_speaker;

      void convert_coref(void);
      void convert_propositions(void);
      void convert_speakers(void);
      void convert_tokens(void);

    public:
      Converter(void);

      void reset(void);
      Doc *convert(const ODocument &odoc);
    };

  }
}

#endif
