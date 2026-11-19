# Career Counseling & Psychometric Assessment App (Android)

A native Android application built with **Kotlin** and **Jetpack Compose** that streamlines student career counseling, psychometric personality assessments, career path matching, appointment scheduling, and administrative management.

---

## Features

### Student Portal
- **Psychometric Assessments**: Interactive personality and aptitude questionnaires (`AssessmentScreen`) evaluating core personality traits.
- **Detailed Assessment Reports**: Visual trait breakdowns (`TraitProgressBar`), tailored career recommendations (`CareerCard`), and downloadable/shareable PDF reports (`PdfReportGenerator`).
- **Progress Dashboard**: Track assessment history, trait scores, and career readiness over time (`ProgressDashboardScreen`).
- **Counseling Appointments**: Book and manage 1-on-1 counseling sessions (`StudentAppointmentsScreen`) with email notification support (`EmailNotificationHelper`).

### Admin / Counselor Portal
- **Admin Dashboard**: Centralized overview of student activity, assessments, and upcoming sessions (`AdminDashboardScreen`).
- **Student Roster**: View and manage registered student profiles and assessment records (`AdminStudentRosterScreen`).
- **Content Management**: Manage assessment questions and career path entries (`AdminContentManagementScreen`).
- **Cloud Backup & Sync**: Manage data backup and restoration (`CloudBackupScreen`, `CloudStorageManager`).
- **Role Switcher**: Seamlessly switch between Student and Admin roles (`ProfileRoleSwitcherDialog`).

---

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (`CareerCounselingViewModel` + `CareerCounselingRepository`)
- **Local Database**: Room Persistence Library (SQLite) with DAOs and TypeConverters
- **Reporting & Utilities**: Android PDF generation (`PdfReportGenerator`) & Email notifications (`EmailNotificationHelper`)
- **Testing**: JUnit, Robolectric, and Screenshot Testing
- **Build System**: Gradle (Kotlin DSL) with Version Catalogs (`libs.versions.toml`)

---

## Project Structure

```text
app/src/main/java/com/example/
├── data/
│   ├── model/         # UserProfile, UserRole, PersonalityTrait, CareerPath, AssessmentQuestion, AssessmentResult, CounselingAppointment
│   ├── local/         # AppDatabase, Converters, and Room DAOs
│   └── repository/    # CareerCounselingRepository, CloudStorageManager
├── ui/
│   ├── viewmodel/     # CareerCounselingViewModel
│   ├── components/    # CareerCard, TraitProgressBar, ProfileRoleSwitcherDialog
│   ├── screens/
│   │   ├── student/   # Home, Assessment, Report, Progress Dashboard, Appointments
│   │   └── admin/     # Admin Dashboard, Student Roster, Content Management, Cloud Backup
│   └── theme/         # Color, Theme, Type
├── util/              # PdfReportGenerator, EmailNotificationHelper
└── MainActivity.kt
