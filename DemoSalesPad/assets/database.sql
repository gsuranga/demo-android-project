DROP TABLE IF EXISTS tbl_timestamp;
DROP TABLE IF EXISTS tbl_user_details;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS Dealer;
DROP TABLE IF EXISTS Purchase_bill;
DROP TABLE IF EXISTS purchase_items;
DROP TABLE IF EXISTS NewDealer;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS vehicale;
DROP TABLE IF EXISTS garages;
DROP TABLE IF EXISTS sales_info;


CREATE TABLE tbl_timestamp (id_timestamp INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, timestamp_value TEXT);
CREATE TABLE tbl_user_details (id_user INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user_id INTEGER, username TEXT, password TEXT, name TEXT);
CREATE TABLE item (
  item_id         TEXT PRIMARY KEY NOT NULL,
  item_part_no    TEXT             NOT NULL,
  description     TEXT             NOT NULL,
  selling_price   TEXT             NOT NULL,
  total_stock_qty TEXT             NOT NULL,
  status          TEXT             NOT NULL,
  time_stamp      TEXT             NOT NULL,
  avg_movement_in_area      TEXT   NOT NULL
);

CREATE TABLE "Dealer" (
  "ID"                INTEGER ,
  "delar_id"          TEXT     PRIMARY KEY                         NOT NULL,
  "delar_account_no"  TEXT                              NOT NULL,
  "delar_name"        TEXT                              NOT NULL,
  "delar_address"     TEXT,                            
  status              TEXT,
  discount_percentage TEXT,
  username            TEXT,
  dealer_password     TEXT,
  shop_name           TEXT,
  so_update_status    TEXT,
  current_to          TEXT,
  credit_limit              TEXT,
  outstanding_amount        TEXT, 
  overdue_amount            TEXT,
  lat TEXT DEFAULT '0',
  long TEXT DEFAULT '0',
  sync_status INTEGER DEFAULT 1

);

CREATE TABLE "Purchase_bill" (
  "ID"                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "BillAmount"          TEXT                              NOT NULL,
  "dealer_id"            TEXT                              NOT NULL,
  "dealer_name"         TEXT                              NOT NULL,
  "date_of_bill"        TEXT                              NOT NULL,
  "lon"                 TEXT                              NOT NULL,
  "lat"                 TEXT                              NOT NULL,
  "b_level"             TEXT                              NOT NULL,
  "status"              TEXT                              NOT NULL,
  "syncstatus"          TEXT DEFAULT 'NOT SYNC',
  "BillAmount_with_vat" TEXT,
   discount_percentage  TEXT, 
   tour_iternery_status      TEXT DEFAULT '0',
    finish_status      TEXT DEFAULT '0',
   account_no      TEXT,
   remark_one  TEXT,
   remark_two TEXT,
   is_call_order TEXT DEFAULT '0'   
    

);

CREATE TABLE "purchase_items" (
  "ID"          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "BillID"      TEXT                              NOT NULL,
  "description" TEXT                              NOT NULL,
  "item_id"     TEXT                              NOT NULL,
  "qty"         TEXT                              NOT NULL,
 "price"        TEXT                               NOT NULL,
  part_no     TEXT                                 NOT NULL,
  comment      TEXT,
  UNIQUE(BillID,item_id)
);

CREATE TABLE "NewDealer" (
  "ID"            INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "delar_name"    TEXT                              NOT NULL,
  "delar_address" TEXT                              NOT NULL,
  "shop_name"     TEXT                              NOT NULL

);


CREATE TABLE "customer" (
  "ID"               INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "customer_name"    TEXT                              NOT NULL,
  "customer_address" TEXT                              NOT NULL,
  "contact_no"       TEXT                              NOT NULL

);


CREATE TABLE "vehicale" (
  "ID"          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "vehicale_no" TEXT   UNIQUE                     NOT NULL,
  "user_id"     TEXT , 
   type          TEXT               
);

CREATE TABLE "garages" (
 "ID"             INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                   
  garage_name     TEXT                              NOT NULL,
 "g_address"      TEXT                              NOT NULL,
 "g_contact_no"   TEXT                              NOT NULL,
 "nearest_dealer" TEXT                              NOT NULL,
 "remarks"        TEXT                              NOT NULL,
  g_id            TEXT          


);

CREATE TABLE sales_info (

  "Dealer_ID"                                 TEXT,
  "ItemID"                                    TEXT,
  "Part_Number"                               TEXT,
  "Available_Stocks_at_the_Dealer"            TEXT,
  "avg_monthly_sale"                          TEXT,
  "Total_Sales_for_last_30days"               TEXT,
  "Stocklostsales"                            TEXT,
  "Valuelostsales"                            TEXT,
  "AverageDailyDemand"                        TEXT,
  "Daysbetweenorders"                         TEXT,
  "SuggestedQty"                              TEXT,
  "Available_Stocks_at_VSD"                   TEXT,
  "UnsuppliedOrderQtyfor90day"                TEXT,
  "movement_in_area_per_month"                TEXT,
  "Days_since_Last_Invoice_Date"              TEXT,
  "Days_since_Last_PO_Date"                   TEXT,
  "Avg_monthly_requirement"                   TEXT,
  "Number_of_Items_invoice_for_past_01_month" TEXT,
  PRIMARY KEY (Dealer_ID, ItemID)
);
CREATE TABLE target_dealer (
  "ID"                                INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  marketing_activity_id               TEXT,
  account_no                          TEXT,
  current_to                          TEXT,
  expected_increase_after_three_month TEXT,
  campaign_ID                         TEXT

);
CREATE TABLE marketing_activity (
  "ID"                        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  campaign_type               TEXT,
  campaign_date               TEXT,
  objective                   TEXT,
  material_required_for_ho    TEXT,
  other_requirment_for_branch TEXT,
  location                    TEXT,
  invitees                    TEXT,
  dimo_employees              TEXT,
  no_of_employees             TEXT,
  quotation                   TEXT,
  budget                      TEXT ,
  priority                    TEXT 

  
);
CREATE TABLE description_table_marketing_activity (
  ID          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  description TEXT,
  ecpu        TEXT,
  qty         TEXT,
  total       TEXT,
  campaign_ID TEXT
);

CREATE TABLE deliver_order (
  deliver_order_id TEXT PRIMARY KEY,
  purchase_order_id TEXT,
  invoice_no           TEXT,
  dealer_id            TEXT,
  added_date           date,
  total_amount         TEXT,
  added_time           TEXT,
  wip_no               TEXT,
  due_date             TEXT,
  status               TEXT,
  accepted_by          TEXT,
  cash_payment         TEXT,
  cheque_payment       TEXT,
  bank_dep_payment     TEXT,
  total_payment        TEXT,
  pending_amount       TEXT, 
  unrealized_cheque    TEXT,
  delar_account_no     TEXT,
  total_paid_amount_with_unrealized_cheques  TEXT,
  total_paid_amount_without_unrealized_cheques TEXT,
  total_pending_amount_with_unrealized_cheques TEXT,
  total_pending_amount_without_unrealized_cheques TEXT,
  realized_cheque_amount TEXT,
  unrealized_cheque_amount TEXT,
  return_amount TEXT,
  number_of_days TEXT,
  iternery_status      TEXT DEFAULT '0',
  target_collection_date TEXT 
);

CREATE TABLE PaymentSending (
  ID        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  deliver_order_id TEXT, TEXT,
  payment_mode        TEXT,
  cash_payment         TEXT,
  unrealized_cheque_payment       TEXT,
  bank_dep_payment TEXT
   
);



CREATE TABLE tbl_cheque_payment (
  ID        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  deliver_order_id TEXT,  
  cheque_no TEXT,  
  amount TEXT,
  bankname TEXT,
  realised_date TEXT,
  path TEXT,date TEXT,time TEXT,sync_status INTEGER DEFAULT 0 
  
);



CREATE TABLE tbl_bank_deposit_payment (
  ID        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  deliver_order_id TEXT,  
  slip_no TEXT,  
  amount TEXT,
  bankname TEXT,
  deposit_date TEXT,
  path TEXT,
 date TEXT,time TEXT,account_no TEXT,sync_status INTEGER DEFAULT 0 
  
  

);

CREATE TABLE dealer_stock (

  item_part_no TEXT,  
  description TEXT,  
  remaining_qty TEXT,
  last_stock_date TEXT,
  delar_id TEXT,
  delar_name TEXT,
  delar_account_no TEXT,
  avg_movement_at_dealer TEXT,
  avg_movement_in_area TEXT,
  PRIMARY KEY (item_part_no, delar_account_no)

); 

CREATE TABLE tour_plan (
  ID    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  town TEXT,  
  dealer TEXT,  
  date Text,
  dealer_ID TEXT,
  status TEXT,
  reason TEXT
 ); 
 

CREATE TABLE new_shops_details(
  ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  shop_name TEXT,
  shop_id   TEXT
 );    
 
 
 CREATE TABLE visit_purposes_details(
  visit_purpose_id TEXT  PRIMARY KEY  NOT NULL,
  purpose_name TEXT
  
 );    
 
 
 

 CREATE TABLE visit_categories_details(
 
  category_name TEXT,
  visit_category_id TEXT PRIMARY KEY  NOT NULL
 );    
 


CREATE TABLE visit_history(
  ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  visit_by TEXT,
  visit_date TEXT,
  visit_purpose TEXT,
  description TEXT,
  select_id TEXT,
  visit_category
 );    
 
 CREATE TABLE fast_moving_items(
  ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  item_id TEXT,
  item_part_no TEXT,
  description TEXT,
  quantity TEXT
  
 );    
 
 CREATE TABLE tour_iternary(
  ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  catogery TEXT,
  outlet TEXT,
  purpose TEXT,
  district TEXT,
  town TEXT,
  route TEXT,
  other_details TEXT
  
 );    
 


CREATE TABLE branding(
  ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  other_details TEXT,
  type TEXT,
  outlet_id TEXT,
  image_path TEXT,
  iternery_status      TEXT DEFAULT '0'
 );    
 



CREATE TABLE failure(
  ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  
  type TEXT,
  outlet_id TEXT,
  part_no TEXT,
  failure TEXT,
  image_path TEXT,
  iternery_status      TEXT DEFAULT '0'
 );   


CREATE TABLE banks(
   bank_id TEXT  PRIMARY KEY  NOT NULL, 
  bank_name TEXT 
  
 );  


CREATE TABLE target(
target_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
dealer_id INTEGER,
year TEXT,
month TEXT,
added_date TEXT,
added_time TEXT,
current_discount_percentage TEXT,
sync_status INTEGER DEFAULT 0);


CREATE TABLE target_item(
target_item_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
target_id INTEGER,item_id INTEGER,
minimum_qty TEXT,
additional_qty TEXT,
current_selling_price TEXT);

CREATE TABLE competitor_part(
cp_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
outlet_cat_id INTEGER,
outlet_id INTEGER,
added_time TEXT(1000),
added_date TEXT(1000),
sync_status INTEGER DEFAULT 0);

CREATE TABLE competitor_part_item(
cpi_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
cp_id INTEGER,
item_id TEXT,
part_number TEXT(1000),
brand TEXT(1000),
importer TEXT(1000),
cost_price_to_the_dealer TEXT(1000),
selling_price_to_the_customer TEXT(1000),
average_monthly_movement TEXT(1000),
overall_movement_at_the_dealer TEXT(1000),
upload_image_path TEXT(1000));

CREATE TABLE tour_itenary(ti_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
outlet_id INTEGER,outlet_name TEXT,visit_category TEXT,
visit_purpose TEXT,visit_time TEXT,visit_date TEXT,description TEXT,status  INTEGER DEFAULT 0);

CREATE TABLE IF NOT EXISTS payment_return_item(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
deliver_order_id INTEGER,item_id INTEGER,qty TEXT,amount TEXT,remarks TEXT,path TEXT,reason_id INTEGER,date TEXT,time TEXT,sync_status INTEGER DEFAULT 0);


CREATE TABLE tbl_garage_loyalty (
  ID        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  deliver_order_id TEXT,voucher_no TEXT,  
  amount TEXT,remarks TEXT,path TEXT,
  date TEXT,time TEXT,sync_status INTEGER DEFAULT 0 
);

