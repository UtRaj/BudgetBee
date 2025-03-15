
# **BudgetBee**


# 🛠️ **Tech Stack**

An overview of the technologies powering the BudgetBee application.

---

<details>
<summary>⚙️ **Backend** - Java & Spring Boot</summary>

- **Language:** Java 17  
- **Framework:** Spring Boot  
- **Database Interaction:** Spring Data JPA  
- **Security:** Spring Security, OAuth2 Resource Server  
- **Validation:** Hibernate Validator  
- **Testing:** JUnit, Spring Security Test  
- **Build Tool:** Maven  

</details>

---

<details>
<summary>💻 **Frontend** - ReactJS</summary>

- **Language:** JavaScript (Node.js 18.15.0)  
- **Framework:** ReactJS  
- **UI Library:** Material UI (MUI), Bootstrap  
- **State Management:** Redux, Redux Thunk, Redux Persist  
- **Routing:** React Router  
- **Charts & Visualization:** Chart.js, React-Chartjs-2  
- **HTTP Requests:** Axios  
- **Authentication:** bcryptjs, js-cookie  
- **Testing:** Jest, React Testing Library  

</details>

---

<details>
<summary>🗄️ **Database** - MySQL</summary>

- **Database System:** MySQL  
- **ORM:** Hibernate (JPA)  
- **Connector:** `mysql-connector-java`  
- **Persistence API:** Jakarta Persistence API  

</details>

---

# 🚀 **Features Overview**

A web application meant to manage one's personal finances, track bank accounts and visualize expenses with ease.  

---

<details>
<summary>🔐 **Authentication Workflow**</summary>

- User can **Sign Up** and **Log In** securely.  
- Password validation ensures secure signup (password & confirm password must match).  
- Redirects users to the **Dashboard** upon successful login.  
- User-friendly error notifications for incorrect credentials.  

</details>

---

<details>
<summary>📊 **Dashboard Module**</summary>

- Visualizes finances with charts for:  
  - Account Balance  
  - Income  
  - Expenses  
  - Categories  
- Filter charts based on **All Time** or **Last 15 Days**.  
- Responsive design for seamless navigation across devices.  

</details>

---

<details>
<summary>📂 **Account Management**</summary>

- View a list of all user accounts.  
- Add new accounts with validation checks.  
- Edit existing account details with real-time updates.  
- Delete accounts securely with confirmation prompts.  
- Real-time toaster notifications for user actions.  

</details>

---

<details>
<summary>💸 **Transaction Module**</summary>

- View all **Income** and **Expense** transactions.  
- Filter transactions by specific accounts.  
- Add new transactions with fields for:  
  - Account selection  
  - Category selection (with option to create a new category)  
  - Date and Amount  
  - Description  
- Edit and delete transactions easily.  
- Real-time feedback with confirmation notifications.  

</details>

---

<details>
<summary>📁 **Category Management**</summary>

- Add custom categories for expense/income tracking.  
- Edit or delete categories as needed.  
- Categories auto-sync with the transaction module for seamless integration.  

</details>

---

<details>
<summary>🔄 **Mock Bank API Integration**</summary>

- Fetches transactions directly from a **Mock Bank API**.  
- Integrates both manual and automated transaction data for consistency.  
- Real-time synchronization with the user's dashboard and reports.  

</details>

---

# 🚀 **How to Build and Run FINTRACK**

Follow these step-by-step instructions to set up, build, and run the FINTRACK project.

---

<details>
<summary>🔧 **1. Setup Prerequisites**</summary>

- Install **Java 17**  
- Install **Node.js (version 18.15.0)**  
- Install **MySQL Database**  
- Install **Maven** (for backend)  
- Install **npm** (for frontend)  

</details>


---


<details>
<summary>⚙️ **2. Build & Run the Backend**</summary>

1. Navigate to the backend folder. 

2. Ensure your local MySQL application is working. Also, change the **application.properties** file as needed.
   
3. Build the backend using Maven:

```

mvn clean package spring-boot:repackage -DskipTests=true

```

4. Run the generated JAR file

```

java -jar ./target/backend-0.0.1-SNAPSHOT.jar

```

5. ✅ Backend will start running at: http://localhost:8080

</details>


---


<details> 
<summary>💻 **3. Build & Run the Frontend**</summary>

1. Navigate to the frontend folder:

```
cd ../frontend

```

2. Install the necessary dependencies:

```
npm install

```

3. Start the frontend service:

```
npm start

```

4. ✅ Frontend will start running at: http://localhost:3000





