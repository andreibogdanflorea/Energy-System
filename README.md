# Proiect Energy System

## Implementare

### Entitati

- pachetul business:
    * Contract - modeleaza un contract intre un consumator si un distribuitor
    * Formulas - clasa utilitara folosita pentru a calcula diferitele preturi
    * Transactions - clasa folosita pentru distribuirea salariilor, plata
    contractelor si plata intretinerii de catre distribuitori
      
- pachetul entities:
    * BusinessEntity - clasa abstracta folosita pentru modelarea unei entitati 
      ce are un buget (consumatori si distribuitori)
    * Consumer - modeleaza un consumator, contine un Contract si poate avea
    o datorie (modelata ca o clasa interna, Debt)
    * Distributor - modeleaza un distribuitor, contine informatii despre 
      contractele sale, despre preturile sale si despre producatori si energia 
      procurata
    * EnergyEntity - interfata implementata de BusinessEntity si Producer, 
      folosita doar pentru ca Factory-ul sa poata fi folosit si pentru
      Producatori
    * EnergyEntityFactory - design pattern-ul Factory folosit pentru a crea
      obiecte de tip Consumer, Distributor sau Producer, folosit impreuna
      cu Singleton
    * EnergyType - folosit in cadrul producatorilor pentru a da o semnificatie
      tipului de energie produs de acestia
    * Producer - modeleaza un producator, contine informatii despre energia
      oferita de acesta, despre distribuitorii sai si despre statisticile
      lunare
      
- pachetul fileio:
    * EnergyEntityInput - interfata implementata de ConsumerInput, 
      DistributorInput si ProducerInput, folosita pentru a face posibil
      Factory-ul de entitati
    * Input, ConsumerInput, DistributorInput, ProducerInput, 
      MonthlyUpdateInput, InitialInputData, CostChangesInput,
      ProducerChangesInput - clase auxiliare folosite pentru parsarea
      input-ului
    * Output, ConsumerOutput, ContractOutput, DistributorOutput,
      ProducerOutput, MonthlyStatsOutput - clase auxiliare pentru scrierea 
      output-ului
    * InputParser, OutputWriter - contin logica din spatele parsarii
      input-ului si scrierii output-ului
      
- pachetul simulation:
    * MonthlySimulation - folosit pentru simularea rundelor (a lunilor),
      contine si o referinta la baza de date folosita in simulare
    * SimulationDatabase - baza de date  ce contine toti consumatorii, 
      distribuitorii, producatorii si  actualizarile lunare; este actualizata
      in ordinea corecta de catre MonthlySimulation

- pachetul strategies
    * ChooseProducersStrategy - clasa abstracta ce contine o metoda generala
      pentru alegerea producatorilor de catre un distribuitor
    * EnergyChoiceStrategyType - tipuri de strategii pe care le pot adopta
    distribuitorii pentru a alege producatorii lor
    * GreenStrategy, PriceStrategy, QuantityStrategy - extind clasa abstracta
      ChooseProducersStrategy si implementeaza in mod specific criteriul de
      comparare folosit pentru alegerea producatorilor
    * StrategyFactory - design pattern-ul Factory folosit pentru a crea
      una din cele trei strategii, folosit impreuna cu Singleton

* Main - clasa ce are rol de a apela succesiv partile din implementare
necesare pentru simulare
  
### Flow

Din clasa Main se parseaza input-ul cu ajutorul clasei InputParser, care la
randul ei se foloseste de clasele desemnate pentru citire (InitialInputData,
CostChangesInput, etc.).

Dupa aceea, se creeaza o noua simulare (MonthlySimulation), impreuna cu o
noua instanta a bazei de date ce contine listele de consumatori, distribuitori
si producatori. Se incepe simularea rundelor cu ajutorul acestei clase.

Aici, are loc o runda initiala, in care se actualizeaza SimulationDatabase 
in felul urmator:
- distribuitorii isi aleg producatorii
- distribuitorii isi actualizeaza preturile
- consumatorii semneaza contracte

Tot in runda initiala, se efectueaza tranzactiile cu ajutorul clasei
Transactions: consumatorii primesc salariile, platesc contractele, iar apoi
distribuitorii isi platesc intretinerea.

Dupa runda initiala, au loc o serie de runde (luni) in MonthlySimulation, 
in care:
- se actualizeaza SimulationDatabase conform inceputului de luna: se adauga 
  consumatorii noi, se modifica preturile distribuitorilor si se semneaza noi
  contracte
- se efectueaza tranzactiile cu ajutorul clasei Transactions
- se actualieaza SimulationDatabase conform sfarsitului de luna: se scot
  contractele consumatorilor ce au ramas fara bani, se actualizeaza preturile
  producatorilor, distribuitorii ai caror producatori si-au schimbat pretul
  isi aplica din nou strategia de alegere a producatorilor, si in cele din
  urma se adauga statisticile lunii pentru producatori


### Elemente de design OOP

In implementare, am folosit diverse concepte OOP, precum:

- Abstractizare: folosita de exemplu in factory-uri, fiindca nu e nevoie
  sa se stie exact tipul returnat

- Incapsulare: folosita in aproape toate clasele (prin gettere si settere),
  iar de exemplu clasele Transactions, MonthlySimulation si SimulationDatabase
  contin si metode private, deoarece un utilizator nu ar avea explicita
  nevoie de ele, ci de metodele care apeleaza aceste metode private

- Mostenire: folosita pentru Consumer si Distributor, pentru ca ambele au
  in comun un id, un buget si starea de a fi sau nu falimentat; folosita
  de asemenea la strategii: toate cele trei strategii mostenesc o clasa
  abstracta in care este implementata o metoda comuna folosita la fel de
  toate strategiile, cu exceptia criteriului de comparare folosit

### Design patterns

- Singleton: folosit in implementarea claselor care sunt Factory, 
  EnergyEntityFactory si StrategyFactory
  
- Factory: folosit pentru crearea consumatorilor, a distribuitorilor si
  a producatorilor in cadrul EnergyEntityFactory, dar si pentru crearea
  strategiilor in StrategyFactory
  
- Observer: distribuitorii sunt observatori si producatorii sunt observabili.
  Atunci cand un producator isi modifica cantitatea de energie, 
  distribuitorii sai sunt notificati si se marcheaza faptul ca au nevoie
  de o actualizare a producatorilor, ce va fi facuta ulterior
  
- Strategy: distribuitorii au asociat un tip de strategie, care va fi folosit
  atunci cand este necesara actualizarea producatorilor

### Dificultati intalnite, limitari, probleme

La prima etapa, implementasem tranzactiile folosind design pattern-ul visitor,
dar la aceasta etapa mi-am dat seama ca nu este intocmai in regula, fiindca
decupleaza o parte din logica care nu ar trebui decuplata, si anume plata
contractelor: cand un consumator face o plata spre un distribuitor,
distribuitorul trebuie de asemenea sa primeasca plata in acelasi timp. Altfel,
se complica fara motiv implementarea.
