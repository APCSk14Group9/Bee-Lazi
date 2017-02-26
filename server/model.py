from sqlalchemy import create_engine
from sqlalchemy.orm import relationship
engine = create_engine('sqlite:///:memory:', echo=False)
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Sequence, ForeignKey 
Base = declarative_base()
class User(Base):
    __tablename__ = 'users'
    username = Column(String,primary_key=True)
    fullname = Column(String)
    password = Column(String)
    imagelink = Column(String, default = 'http://kenh14cdn.com/thumb_w/600/dpA6uSv3GtBzvbRT7Y4EBtfN37yCA/Image/2014/08/dc1-94764.jpg')
    favorites = relationship("Favorite",back_populates='user', cascade ='all, delete, delete-orphan')
    event = relationship("Event", back_populates='user', cascade ='all,delete,delete-orphan')
    def __repr__(self):
        return '{"username":"%s", "fullname" : "%s", "password" : "%s", "imagelink" = "%s"}' %(self.username, self.fullname, self.password, self.imagelink)

class Favorite(Base):
    __tablename__ = 'favorite'
    username = Column(String,   ForeignKey('users.username'), primary_key = True, nullable=False)
    user = relationship("User", back_populates= 'favorites')
    # list of favorite
    Solemnoftheplace = Column(String,default = '50')
    Sweet = Column(String,default ='50')
    Sour = Column(String,default ='50')
    Salty = Column(String,default ='50')
    Bitter = Column(String,default ='50')
    Spicy = Column(String,default ='50')
    Umami = Column(String,default ='50')
    Dessert = Column(String,default ='50')
    Privacy = Column(String,default ='50')
    Amount = Column(String,default ='50')
    Snacking = Column(String,default ='50')
    Amountofcalories = Column(String,default ='50')
    Cost = Column(String, default = '50')
    Seriesornot = Column(String,default = '0')
    Duration = Column(String,default='50')
    Action = Column(String,default='50')
    Comedy = Column(String,default='50')
    Horror = Column(String,default='50')
    FamilySocialCommunity = Column(String,default='50')
    Romantic = Column(String,default='50')
    ScienceFiction = Column(String,default='50')
    Documentary = Column(String,default='50')
    Classic = Column(String,default='50')
    ChildAnimation = Column(String,default='50')
    Time = Column(String,default='50')
    def __repr__(self):
        return '["%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s"]' %(self.Solemnoftheplace,self.Sweet,self.Sour,self.Salty,self.Bitter,self.Spicy,self.Umami,self.Dessert,self.Privacy,self.Amount,self.Snacking,self.Amountofcalories,self.Cost,self.Seriesornot,self.Duration,self.Action,self.Comedy,self.Horror,self.FamilySocialCommunity,self.Romantic,self.ScienceFiction,self.Documentary,self.Classic,self.ChildAnimation,self.Time)
    def food(self):
        return [self.Cost,self.Solemnoftheplace,self.Sweet,self.Sour,self.Salty,self.Bitter,self.Spicy,self.Umami,self.Dessert,self.Privacy,self.Amount,self.Snacking,self.Amountofcalories]
    def film(self):
        return [self.Seriesornot,self.Duration,self.Action,self.Comedy,self.Horror,self.FamilySocialCommunity,self.Romantic,self.ScienceFiction,self.Documentary,self.Classic,self.ChildAnimation]

class Event(Base):
    __tablename__ = 'event'
    username = Column(String, ForeignKey('users.username'), nullable= False)
    eventid = Column(Integer, Sequence('event_id_seq') , primary_key = True)
    title = Column(String)
    startdate = Column(String)
    enddate = Column(String)
    starttime = Column(String)
    endtime = Column(String)
    user = relationship('User', back_populates='event')
    def __repr__(self):
        return '{"title":"%s","starttime":"%s","startdate":"%s","endtime":"%s","enddate":"%s"}' %(self.title, self.starttime, self.startdate,self.endtime, self.enddate)

    
class Food(Base):
    __tablename__ = 'food'
    Type = Column(String,default = 'Food')
    Name = Column(String, primary_key = True)
    Place  = Column(String)
    Address = Column(String)
    Url  = Column(String)
    Cost = Column(String,default ='50')
    Time = Column(String,default = '0-24')
    Solemnoftheplace = Column(String,default ='50')
    Sweet = Column(String,default ='50')
    Sour = Column(String,default ='50')
    Salty = Column(String,default ='50')
    Bitter = Column(String,default ='50')
    Spicy = Column(String,default ='50')
    Umami = Column(String,default ='50')
    Dessert = Column(String,default ='50')
    Privacy = Column(String,default ='50')
    Amount = Column(String,default ='50')
    Snacking = Column(String,default ='50')
    Amountofcalories = Column(String,default ='50')
    FoodImg = Column(String)
    def __repr__(self):
        return '["%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s"]' %(self.Type,self.Name,self.Place,self.Address,self.Url,self.Cost,self.Time,self.Solemnoftheplace,self.Sweet,self.Sour,self.Salty,self.Bitter,self.Spicy,self.Umami,self.Dessert,self.Privacy,self.Amount,self.Snacking,self.Amountofcalories,self.FoodImg)

class FoodAdd(Base):
    __tablename__ = 'foodadd'
    foodname = Column(String, primary_key =True)
    place = Column(String, primary_key = True)
    address = Column(String)
    linkanh = Column(String)
    price = Column(String)
    def __repr__(self):
        return '["%s","%s","%s","%s"]' %(self.place, self.address, self.linkanh, self.price) 

class Film(Base):
    __tablename__ = 'films'
    Type = Column(String, default = 'Film')
    Name = Column(String, primary_key= True)
    Link = Column(String)
    Description = Column(String)
    Seriesornot = Column(String,default = '0')
    Duration = Column(String,default='50')
    Action = Column(String,default='50')
    Comedy = Column(String,default='50')
    Horror = Column(String,default='50')
    FamilySocialCommunity = Column(String,default='50')
    Romantic = Column(String,default='50')
    ScienceFiction = Column(String,default='50')
    Documentary = Column(String,default='50')
    Classic = Column(String,default='50')
    ChildAnimation = Column(String,default='50')
    Time = Column(String,default='50')
    Linkanh = Column(String)
    def __repr__(self):
        return '["%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s"]'%(self.Type,self.Name,self.Link,self.Description,self.Seriesornot,self.Duration,self.Action,self.Comedy,self.Horror,self.FamilySocialCommunity,self.Romantic,self.ScienceFiction,self.Documentary,self.Classic,self.ChildAnimation,self.Time,self.Linkanh)

class History(Base):
    __tablename__ = 'histories'
    username = Column(String, ForeignKey('users.username'), primary_key = True)
    itemname = Column(String,primary_key = True)
    itemlink = Column(String)
    itemposition = Column(String)
    Type = Column(String)
    def __repr__(self):
        return '["%s","%s","%s","%s"]' %(self.itemname,self.itemlink,self.Type,self.itemposition)


Base.metadata.create_all(engine)
from sqlalchemy.orm import sessionmaker
Session = sessionmaker(bind=engine)
session = Session()   

