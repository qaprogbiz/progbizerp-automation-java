📌 Setup Instructions

🔹 First-Time Pull Setup

After cloning or pulling the project for the first time, please create the following folders in the project root:

1. Screenshot/

   * Used to store runtime screenshots generated during test execution.

2. testdata/

   * Used to store dynamically generated Excel files for customer upload testing.

⚠️ Notes:

* These folders are **not tracked in Git** and must be created manually.
* Do NOT add generated files inside these folders to Git.

---

🔹 Customer Upload Automation Setup

* The automation framework dynamically generates Excel files during execution for customer upload testing.
* Files will be created inside the `testdata/` folder at runtime.
* No manual Excel file creation is required.

✔ Ensure:

* The `testdata/` folder exists before running tests.
* The framework has permission to write files in this location.

---

🔹 Execution Notes

* Screenshots will be saved in the `Screenshot/` folder on failure or as configured.
* Excel files are generated fresh for each run to avoid duplicate data issues.
* Do not modify or reuse generated Excel files manually.
