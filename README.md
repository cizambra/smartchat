# smartchat
Smart chat


To use the Parser and the RDF repo client to get answers to questions
 
 String sentence = "what is the quantity of fnSku1 on orderId1";
 
 
 // Parse
 Parser parser = new Parser();
 
 RDFSentence rdfSentence = parser.parse(sentence8);
 
 RDFQuestion rdfQuestion = Converter.convertToRDFQustion(rdfSentence);
 
 
 
 // Ask Repo
 ProcurementRDFRepository repo = new ProcurementRDFRepository("http://localhost:8080/openrdf-sesame/", "procurement");
 
 String JSONAnswer = repo.ask(rdfQuestion);

 
