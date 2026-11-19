package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.AssessmentDeadline
import com.example.data.model.AssessmentQuestion
import com.example.data.model.AssessmentResult
import com.example.data.model.CareerPath
import com.example.data.model.CounselingAppointment
import com.example.data.model.EducationLevel
import com.example.data.model.PersonalityTrait
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CareerCounselingRepository(
    private val context: Context,
    private val database: AppDatabase
) {
    private val userDao = database.userProfileDao()
    private val assessmentDao = database.assessmentResultDao()
    private val careerDao = database.careerPathDao()
    private val questionDao = database.assessmentQuestionDao()
    private val appointmentDao = database.appointmentDao()

    val cloudStorageManager = CloudStorageManager(context, database)

    // Data streams
    val allProfiles: Flow<List<UserProfile>> = userDao.getAllProfiles()
    val allCareers: Flow<List<CareerPath>> = careerDao.getAllCareers()
    val allQuestions: Flow<List<AssessmentQuestion>> = questionDao.getAllQuestions()
    val activeQuestions: Flow<List<AssessmentQuestion>> = questionDao.getActiveQuestions()
    val allResults: Flow<List<AssessmentResult>> = assessmentDao.getAllResults()
    val allAppointments: Flow<List<CounselingAppointment>> = appointmentDao.getAllAppointments()
    val allDeadlines: Flow<List<AssessmentDeadline>> = appointmentDao.getAllDeadlines()

    fun getResultsForStudent(studentId: Long): Flow<List<AssessmentResult>> =
        assessmentDao.getResultsForStudent(studentId)

    fun getLatestResultForStudent(studentId: Long): Flow<AssessmentResult?> =
        assessmentDao.getLatestResultForStudent(studentId)

    fun getAppointmentsForStudent(studentId: Long): Flow<List<CounselingAppointment>> =
        appointmentDao.getAppointmentsForStudent(studentId)

    suspend fun getProfileById(id: Long): UserProfile? = userDao.getProfileById(id)

    suspend fun insertProfile(profile: UserProfile): Long = userDao.insertProfile(profile)

    suspend fun updateProfile(profile: UserProfile) = userDao.updateProfile(profile)

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        // Check if database needs seeding
        if (careerDao.getCount() == 0) {
            seedCareers()
        }
        if (questionDao.getCount() == 0) {
            seedQuestions()
        }
        val existingProfiles = userDao.getAllProfiles().first()
        if (existingProfiles.isEmpty()) {
            seedProfilesAndHistory()
        }
    }

    private suspend fun seedCareers() {
        val initialCareers = listOf(
            CareerPath(
                title = "Fine Arts – Drawing and Painting",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.REALISTIC,
                educationRequirement = "Bachelor's of Fine Arts (BFA) / Studio Arts",
                salaryRange = "$48,000 - $85,000 / year",
                growthOutlook = "Steady (+6% over 10 yrs)",
                keySkills = "Illustration, Color Theory, Canvas Oil/Acrylic, Traditional & Digital Painting, Spatial Composition",
                recommendedMajors = "Fine Arts, Visual Arts, Studio Arts, Illustration",
                description = "Expresses aesthetic beauty, cultural themes, and emotional stories through original drawings, paintings, and sculptured works.",
                academicRoadmap = "Build an extensive portfolio of original artwork, enter regional and national exhibitions, master diverse visual media.",
                specializations = "Fine Artist, Illustrator, Portrait Artist, Sculptor",
                psyAnalysisRating = "Very High:100",
                skillsAbilitiesRating = "High:60",
                recommendationComment = "Top Choice"
            ),
            CareerPath(
                title = "Interior Design",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Bachelor's in Interior Design or Interior Architecture",
                salaryRange = "$58,000 - $98,000 / year",
                growthOutlook = "Average (+8%)",
                keySkills = "3D Spatial Layout, AutoCAD, Color Harmony, Material Selection, Lighting Design, Building Codes",
                recommendedMajors = "Interior Design, Spatial Architecture, Environmental Design",
                description = "Plans, structures, and beautifies functional interior spaces for residential homes, corporate offices, and commercial establishments.",
                academicRoadmap = "Study architectural rendering, master AutoCAD/SketchUp, intern with residential and commercial interior design studios.",
                specializations = "Interior Designer, Home Decor Expert, Space Planner",
                psyAnalysisRating = "Very High:82",
                skillsAbilitiesRating = "High:65",
                recommendationComment = "Top Choice"
            ),
            CareerPath(
                title = "Photography",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.REALISTIC,
                educationRequirement = "Bachelor's or Diploma in Photography / Visual Media",
                salaryRange = "$42,000 - $78,000 / year",
                growthOutlook = "Steady (+9%)",
                keySkills = "Optics, Studio Lighting, Portraiture, Adobe Lightroom & Photoshop, Visual Storytelling",
                recommendedMajors = "Commercial Photography, Digital Arts, Visual Journalism",
                description = "Captures evocative visual stories, commercial campaigns, and portraits with mastery of lighting, perspective, and optics.",
                academicRoadmap = "Develop personal portfolio gallery website, intern with commercial photo studios, curate photographic series.",
                specializations = "Photographer, Photo Editor, Commercial Shooter",
                psyAnalysisRating = "Very High:81",
                skillsAbilitiesRating = "Very High:80",
                recommendationComment = "Top Choice"
            ),
            CareerPath(
                title = "Liberal Arts",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.SOCIAL,
                educationRequirement = "Bachelor's in Liberal Arts, Humanities or Social Sciences",
                salaryRange = "$52,000 - $92,000 / year",
                growthOutlook = "Average (+10%)",
                keySkills = "Critical Inquiry, Interdisciplinary Synthesis, Cultural Analysis, Rhetoric, Creative Writing",
                recommendedMajors = "Humanities, Philosophy, Comparative Literature, Cultural Studies",
                description = "Bridges philosophy, creative expression, sociological inquiry, and critical human thought to understand and shape civilization.",
                academicRoadmap = "Engage in multi-disciplinary research essays, participate in collegiate debates, publish in undergraduate academic reviews.",
                specializations = "Sociologist, Psychologist, Artist, Cultural Essayist",
                psyAnalysisRating = "Very High:81",
                skillsAbilitiesRating = "High:60",
                recommendationComment = "Top Choice"
            ),
            CareerPath(
                title = "Product Design",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Bachelor's in Industrial or Product Design",
                salaryRange = "$75,000 - $125,000 / year",
                growthOutlook = "Fast Growth (+14%)",
                keySkills = "CAD 3D Modeling, Ergonomics, User Prototyping, Materials Engineering, Design Thinking",
                recommendedMajors = "Industrial Design, Human-Centered Design, Mechanical Technology",
                description = "Combines aesthetics, ergonomics, and production technology to invent and design tangible lifestyle products and consumer goods.",
                academicRoadmap = "Master SolidWorks/Rhino, prototype functional physical objects with 3D printing, participate in industrial design competitions.",
                specializations = "Product Designer, Lifestyle Product Designer, UX Product Designer",
                psyAnalysisRating = "Very High:80",
                skillsAbilitiesRating = "Average:56",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Graphic Design",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.ENTERPRISING,
                educationRequirement = "Bachelor's in Graphic Design or Visual Communication",
                salaryRange = "$55,000 - $95,000 / year",
                growthOutlook = "Above Average (+12%)",
                keySkills = "Adobe Illustrator & InDesign, Visual Identity, Typography, Branding Systems, Packaging Design",
                recommendedMajors = "Graphic Design, Digital Media, Communication Arts",
                description = "Produces brand identities, logo marks, corporate publications, advertising assets, and high-impact visual communications.",
                academicRoadmap = "Curate cohesive Behance portfolio with real client case studies, master typography, learn motion graphics basics.",
                specializations = "Graphic Designer, Logo Designer, Print Collateral Designer, Visual Designer",
                psyAnalysisRating = "Very High:79",
                skillsAbilitiesRating = "Very High:87",
                recommendationComment = "Top Choice"
            ),
            CareerPath(
                title = "Counselling Psychology",
                industry = "Human Services",
                primaryTrait = PersonalityTrait.SOCIAL,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Master's Degree in Counseling Psychology / Licensed Counselor",
                salaryRange = "$60,000 - $96,000 / year",
                growthOutlook = "Rapid Growth (+14%)",
                keySkills = "Active Empathetic Listening, Behavioral Intervention, Psychometric Assessment, Mental Wellness Advising",
                recommendedMajors = "Psychology, Behavioral Health, Counseling Psychology",
                description = "Assists individuals and families in navigating psychological challenges, emotional hurdles, life transitions, and relationships.",
                academicRoadmap = "Volunteer at peer counseling hotlines, complete psychological clinic shadow hours, complete state board exams.",
                specializations = "Career Counsellor, Behavioural Counsellor, Relationship Counsellor",
                psyAnalysisRating = "High:69",
                skillsAbilitiesRating = "High:70",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Sociology",
                industry = "Human Services",
                primaryTrait = PersonalityTrait.SOCIAL,
                secondaryTrait = PersonalityTrait.ARTISTIC,
                educationRequirement = "Bachelor's or Master's in Sociology / Social Work",
                salaryRange = "$50,000 - $82,000 / year",
                growthOutlook = "Solid Growth (+11%)",
                keySkills = "Community Fieldwork, Qualitative Research, Policy Advocacy, Cultural Analysis, Human Rights Advocacy",
                recommendedMajors = "Sociology, Public Policy, Community Development",
                description = "Investigates human social relationships, community dynamics, systemic inequities, and mobilizes humanitarian advocacy.",
                academicRoadmap = "Coordinate civic community outreach, publish field sociological studies, intern at international NGOs.",
                specializations = "Social Worker, Human Rights Advocate, NGO Volunteer",
                psyAnalysisRating = "High:68",
                skillsAbilitiesRating = "High:70",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "YouTuber & Digital Content Creator",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.ENTERPRISING,
                educationRequirement = "Self-Directed / Bachelor's in Digital Media or Communications",
                salaryRange = "$45,000 - $140,000+ / year",
                growthOutlook = "Very High (+22%)",
                keySkills = "Video Production, Premiere Pro / DaVinci Resolve, Storyboarding, Audience Analytics, Social Branding",
                recommendedMajors = "Digital Media, Film & Media Studies, Marketing",
                description = "Produces engaging online video content, manages digital communities, and orchestrates creative media storytelling.",
                academicRoadmap = "Produce consistent episodic video content, optimize watch-time retention, study algorithmic marketing strategies.",
                specializations = "Online Video Creator, Video Editor, Digital Marketer, Content Creator",
                psyAnalysisRating = "High:65",
                skillsAbilitiesRating = "High:67",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Architecture",
                industry = "Architecture and Construction",
                primaryTrait = PersonalityTrait.REALISTIC,
                secondaryTrait = PersonalityTrait.ARTISTIC,
                educationRequirement = "Bachelor of Architecture (B.Arch) 5-year Accredited Degree",
                salaryRange = "$82,000 - $135,000 / year",
                growthOutlook = "Steady (+8%)",
                keySkills = "Revit / BIM, Structural Physics, Architectural Drawing, Sustainable Urban Design, Building Codes",
                recommendedMajors = "Architecture, Environmental Architecture, Urban Planning",
                description = "Plans and oversees the structural and aesthetic design of residential buildings, civic monuments, and cityscapes.",
                academicRoadmap = "Maintain a rigorous architectural drawing sketchbook, excel in advanced physics and geometry, prepare for B.Arch admissions.",
                specializations = "Architect, Interior Designer, Structural Engineer",
                psyAnalysisRating = "High:65",
                skillsAbilitiesRating = "High:60",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Multimedia, Animation, Gaming and VFX",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Bachelor's in Animation, Game Art, or VFX",
                salaryRange = "$68,000 - $118,000 / year",
                growthOutlook = "Fast (+16%)",
                keySkills = "3D Rigging, Keyframe Animation, Blender/Maya, Unreal Engine, Particle VFX",
                recommendedMajors = "Computer Animation, Interactive Game Design, VFX Art",
                description = "Enlivens cinematic animations, game character mechanics, visual effects, and immersive 3D digital environments.",
                academicRoadmap = "Build an animation demo reel with character walks and dynamic VFX simulations, submit to student game festivals.",
                specializations = "Animator, 3D Artist, Game Designer, VFX Artist",
                psyAnalysisRating = "High:64",
                skillsAbilitiesRating = "Very High:80",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Web Design, UI and UX",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Bachelor's in HCI, Interaction Design, or Computer Science",
                salaryRange = "$80,000 - $130,000 / year",
                growthOutlook = "High (+18%)",
                keySkills = "Figma, User Testing, Wireframing, Responsive Design, Design Systems, Information Architecture",
                recommendedMajors = "Human-Computer Interaction (HCI), UI/UX Design, Informatics",
                description = "Crafts intuitive, accessible, and delightful digital user interfaces and interaction flows for web and mobile software.",
                academicRoadmap = "Publish comprehensive UX case studies documenting user problems to validated prototypes, master Figma component tokens.",
                specializations = "Web Designer, UI Designer, UX Designer, UX Researcher",
                psyAnalysisRating = "High:64",
                skillsAbilitiesRating = "High:60",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Content Writing",
                industry = "Creative Arts & Design",
                primaryTrait = PersonalityTrait.ARTISTIC,
                secondaryTrait = PersonalityTrait.SOCIAL,
                educationRequirement = "Bachelor's in English Literature, Creative Writing, or Journalism",
                salaryRange = "$48,000 - $88,000 / year",
                growthOutlook = "Average (+9%)",
                keySkills = "Editorial Prose, SEO Copywriting, Narrative Structure, Research Synthesis, Brand Voice",
                recommendedMajors = "Creative Writing, English, Journalism, Mass Communication",
                description = "Weaves narrative depth and informative prose into digital articles, publications, scripts, and brand literature.",
                academicRoadmap = "Publish articles regularly on Medium/Substack, write for school publications, complete copy-editing apprenticeships.",
                specializations = "Creative Writer, Storyteller, Content Creator, Copywriter",
                psyAnalysisRating = "High:61",
                skillsAbilitiesRating = "High:73",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Clinical Psychology",
                industry = "Human Services",
                primaryTrait = PersonalityTrait.SOCIAL,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Ph.D. or Psy.D. in Clinical Psychology",
                salaryRange = "$85,000 - $138,000 / year",
                growthOutlook = "Fast (+14%)",
                keySkills = "Diagnostic Psychopathology, Psychotherapy, Cognitive Behavioral Therapy, Clinical Research",
                recommendedMajors = "Clinical Psychology, Neuroscience, Cognitive Studies",
                description = "Evaluates, diagnoses, and treats acute and chronic mental health conditions through evidence-based psychotherapy.",
                academicRoadmap = "Assist in clinical hospital research labs, complete honors undergraduate thesis, prepare for doctoral internships.",
                specializations = "Mental Health Counsellor, Depression Counsellor, Anxiety Specialist",
                psyAnalysisRating = "High:61",
                skillsAbilitiesRating = "Average:40",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Software & AI Solutions Architect",
                industry = "Technology & Computing",
                primaryTrait = PersonalityTrait.INVESTIGATIVE,
                secondaryTrait = PersonalityTrait.REALISTIC,
                educationRequirement = "Bachelor's or Master's in Computer Science / Software Engineering",
                salaryRange = "$110,000 - $175,000 / year",
                growthOutlook = "Rapid Growth (+26% over 10 yrs)",
                keySkills = "Python, Kotlin, Cloud Architecture, Machine Learning, Systems Design",
                recommendedMajors = "Computer Science, Data Science, Cognitive Systems",
                description = "Designs algorithmic solutions, artificial intelligence models, and distributed software systems for modern tech enterprises.",
                academicRoadmap = "Participate in coding hackathons, build open-source portfolios, take AP Computer Science and linear algebra.",
                specializations = "Cloud Architect, Machine Learning Engineer, Backend Lead",
                psyAnalysisRating = "High:70",
                skillsAbilitiesRating = "High:75",
                recommendationComment = "Good Choice"
            ),
            CareerPath(
                title = "Robotics & Automation Engineer",
                industry = "Engineering & Advanced Manufacturing",
                primaryTrait = PersonalityTrait.REALISTIC,
                secondaryTrait = PersonalityTrait.INVESTIGATIVE,
                educationRequirement = "Bachelor's in Mechatronics, Mechanical or Electrical Engineering",
                salaryRange = "$90,000 - $148,000 / year",
                growthOutlook = "High Growth (+15%)",
                keySkills = "CAD Modeling, Embedded C++, Sensor Integration, Control Systems",
                recommendedMajors = "Mechanical Engineering, Mechatronics, Electrical Engineering",
                description = "Builds physical automated machinery, robotic arms, autonomous vehicles, and industrial sensory systems.",
                academicRoadmap = "Join high school FIRST Robotics / collegiate Formula SAE, master CAD software (SolidWorks/Fusion 360).",
                specializations = "Robotics Engineer, Mechatronics Specialist, Automation Architect",
                psyAnalysisRating = "High:65",
                skillsAbilitiesRating = "High:70",
                recommendationComment = "Good Choice"
            )
        )
        careerDao.insertCareers(initialCareers)
    }

    private suspend fun seedQuestions() {
        val questions = listOf(
            // Section 1: PLANNING STAGE (Aptitude & Decision Readiness)
            AssessmentQuestion(
                questionText = "I have considered several possible careers, but I frequently feel torn, uncertain, or confused about which pathway truly aligns with who I am.",
                studentScenario = "When asked by relatives, teachers, or friends what you want to pursue, you have mixed ideas but no definite certainty.",
                section = "PLANNING_STAGE",
                dimensionKey = "STAGE_CONFUSED",
                dimensionTitle = "Planning Stage: Decision Ambiguity",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 1
            ),
            AssessmentQuestion(
                questionText = "My current thoughts regarding career streams are significantly influenced by the opinions and expectations of parents or peers rather than personal clarity.",
                studentScenario = "You consider a particular field mainly because people around you recommended it or your friends are choosing it.",
                section = "PLANNING_STAGE",
                dimensionKey = "STAGE_INFLUENCE",
                dimensionTitle = "Planning Stage: External Dependencies",
                trait = PersonalityTrait.SOCIAL,
                sortOrder = 2
            ),
            AssessmentQuestion(
                questionText = "I feel the immediate need for structured career counseling, psychometric profiling, and an actionable roadmap to clarify my educational choices.",
                studentScenario = "You want expert guidance to objectively match your hidden abilities with real college majors and market professions.",
                section = "PLANNING_STAGE",
                dimensionKey = "STAGE_COUNSELING",
                dimensionTitle = "Planning Stage: Counseling Readiness",
                trait = PersonalityTrait.INVESTIGATIVE,
                sortOrder = 3
            ),

            // Section 2: CAREER PERSONALITY (MBTI 4-Dimensions)
            AssessmentQuestion(
                questionText = "Where do you prefer to focus your energy? I draw greater inspiration and energy from quiet reflection, inner ideas, and solo focus rather than lively large group gatherings.",
                studentScenario = "After a long busy school day, you recharge best by reading, creating artwork, writing, or spending peaceful time in your personal space.",
                section = "CAREER_PERSONALITY",
                dimensionKey = "EI",
                dimensionTitle = "Energy Focus: Introvert vs Extrovert",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 4
            ),
            AssessmentQuestion(
                questionText = "How do you grasp and process information? I prefer exploring hidden meanings, big-picture visions, and future possibilities rather than strictly memorizing step-by-step facts.",
                studentScenario = "When introduced to a new topic, you immediately envision novel conceptual connections rather than focusing solely on physical data.",
                section = "CAREER_PERSONALITY",
                dimensionKey = "SN",
                dimensionTitle = "Information Processing: iNtuitive vs Sensing",
                trait = PersonalityTrait.INVESTIGATIVE,
                sortOrder = 5
            ),
            AssessmentQuestion(
                questionText = "How do you make important decisions? I make choices based on empathy, personal core values, and human impact, being guided by my heart rather than purely cold detached logic.",
                studentScenario = "In resolving a debate or choosing a project direction, your primary consideration is fairness, human emotion, and harmonious relationships.",
                section = "CAREER_PERSONALITY",
                dimensionKey = "TF",
                dimensionTitle = "Decision Making: Feeling vs Thinking",
                trait = PersonalityTrait.SOCIAL,
                sortOrder = 6
            ),
            AssessmentQuestion(
                questionText = "How do you prefer to plan your work? I prefer a flexible, spontaneous approach to tasks, keeping my options open and juggling ideas rather than adhering to rigid schedules.",
                studentScenario = "You find sudden creative bursts and adaptive working styles much more productive than strictly regimented timetables.",
                section = "CAREER_PERSONALITY",
                dimensionKey = "JP",
                dimensionTitle = "Work Planning: Perceiving vs Judging",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 7
            ),

            // Section 3: CAREER INTEREST TYPES (Holland RIASEC)
            AssessmentQuestion(
                questionText = "Realistic (R): I enjoy hands-on work with physical objects, mechanical equipment, laboratory tools, structural models, or active outdoor environments.",
                studentScenario = "Constructing a physical model, troubleshooting a machine, or working with tangible tools excites your curiosity.",
                section = "CAREER_INTEREST",
                dimensionKey = "R",
                dimensionTitle = "Realistic: Practical & Concrete",
                trait = PersonalityTrait.REALISTIC,
                sortOrder = 8
            ),
            AssessmentQuestion(
                questionText = "Investigative (I): I am energized by analyzing complex problems, scientific exploration, theoretical inquiry, and deducing logical principles.",
                studentScenario = "Investigating why a phenomenon occurs through analytical research and empirical thinking keeps you deeply engaged.",
                section = "CAREER_INTEREST",
                dimensionKey = "I",
                dimensionTitle = "Investigative: Analytical & Inquiring",
                trait = PersonalityTrait.INVESTIGATIVE,
                sortOrder = 9
            ),
            AssessmentQuestion(
                questionText = "Artistic (A): I express myself through imaginative design, visual aesthetics, literary storytelling, fine arts, or creative media in non-conforming settings.",
                studentScenario = "Painting, designing visual graphics, creative writing, or composing multimedia is your natural instinctive outlet.",
                section = "CAREER_INTEREST",
                dimensionKey = "A",
                dimensionTitle = "Artistic: Creative & Expressive",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 10
            ),
            AssessmentQuestion(
                questionText = "Social (S): I am deeply drawn toward helping people, peer counseling, teaching, community welfare, and collaborating with individuals.",
                studentScenario = "Listening to someone's struggles, coaching a peer, or engaging in humanitarian outreach brings you profound satisfaction.",
                section = "CAREER_INTEREST",
                dimensionKey = "S",
                dimensionTitle = "Social: Empathetic & Developmental",
                trait = PersonalityTrait.SOCIAL,
                sortOrder = 11
            ),
            AssessmentQuestion(
                questionText = "Enterprising (E): I enjoy leading initiatives, pitching ideas, persuasive discussions, entrepreneurship, and driving projects to ambitious targets.",
                studentScenario = "Organizing a student venture, delivering a persuasive presentation, or negotiating consensus brings out your natural leadership.",
                section = "CAREER_INTEREST",
                dimensionKey = "E",
                dimensionTitle = "Enterprising: Leadership & Persuasive",
                trait = PersonalityTrait.ENTERPRISING,
                sortOrder = 12
            ),
            AssessmentQuestion(
                questionText = "Conventional (C): I appreciate structured systems, accurate record keeping, organized workflows, cataloging data, and precision auditing.",
                studentScenario = "Setting up clean spreadsheets, maintaining orderly file systems, and following standardized procedures gives you peace of mind.",
                section = "CAREER_INTEREST",
                dimensionKey = "C",
                dimensionTitle = "Conventional: Organized & Detail-Oriented",
                trait = PersonalityTrait.CONVENTIONAL,
                sortOrder = 13
            ),

            // Section 4: CAREER MOTIVATOR TYPES (Core Drivers)
            AssessmentQuestion(
                questionText = "Creativity: I am motivated by the freedom to innovate original concepts, design novel solutions, and express unrestricted imagination.",
                studentScenario = "Being restricted to repetitive boilerplate without creative latitude makes you feel constrained.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "CREATIVITY",
                dimensionTitle = "Motivator: Original Creativity",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 14
            ),
            AssessmentQuestion(
                questionText = "Independence: I perform best when working with autonomy, self-directed freedom, and minimal micro-management.",
                studentScenario = "Given a clear objective, you prefer figuring out your own workflow and pacing without constant oversight.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "INDEPENDENCE",
                dimensionTitle = "Motivator: Autonomy & Independence",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 15
            ),
            AssessmentQuestion(
                questionText = "Adventure & Novelty: I am motivated by exploring new subjects, encountering unconventional challenges, and working in dynamic environments.",
                studentScenario = "You thrive when each week brings distinct assignments rather than a predictable, static routine.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "ADVENTURE",
                dimensionTitle = "Motivator: Adventure & Discovery",
                trait = PersonalityTrait.INVESTIGATIVE,
                sortOrder = 16
            ),
            AssessmentQuestion(
                questionText = "Social Service: It is essential for my future career to directly improve the lives of individuals and make a compassionate societal impact.",
                studentScenario = "Knowing that your day's work uplifted someone or protected a vital human cause is your key metric of success.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "SOCIAL_SERVICE",
                dimensionTitle = "Motivator: Compassionate Social Service",
                trait = PersonalityTrait.SOCIAL,
                sortOrder = 17
            ),
            AssessmentQuestion(
                questionText = "Continuous Learning: I am driven by intellectual curiosity, constantly expanding knowledge, and mastering cutting-edge insights.",
                studentScenario = "You actively read beyond the syllabus and love diving deep into fascinating topics.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "CONTINUOUS_LEARNING",
                dimensionTitle = "Motivator: Lifelong Learning",
                trait = PersonalityTrait.INVESTIGATIVE,
                sortOrder = 18
            ),
            AssessmentQuestion(
                questionText = "Structured Environment: I appreciate clearly defined expectations, transparent organizational rules, and predictable stability.",
                studentScenario = "Clear assignment rubrics, dependable timelines, and well-organized hierarchies give you clarity and comfort.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "STRUCTURED_ENV",
                dimensionTitle = "Motivator: Structured Environment",
                trait = PersonalityTrait.CONVENTIONAL,
                sortOrder = 19
            ),
            AssessmentQuestion(
                questionText = "High-Paced Setting: I thrive in fast-paced, high-energy settings with urgent turnarounds and competitive stakes.",
                studentScenario = "High pressure and adrenaline-fueled deadlines energize your speed rather than draining you.",
                section = "CAREER_MOTIVATOR",
                dimensionKey = "HIGH_PACED",
                dimensionTitle = "Motivator: High Paced Tempo",
                trait = PersonalityTrait.ENTERPRISING,
                sortOrder = 20
            ),

            // Section 5: LEARNING STYLE TYPES (VARK)
            AssessmentQuestion(
                questionText = "Visual Learning: I learn and retain concepts most effectively through diagrams, illustrated charts, infographics, color-coded notes, and video models.",
                studentScenario = "When studying a difficult chapter, drawing a mind map or visual flowchart clarifies the concepts instantly.",
                section = "LEARNING_STYLE",
                dimensionKey = "VISUAL",
                dimensionTitle = "Learning Style: Visual",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 21
            ),
            AssessmentQuestion(
                questionText = "Auditory Learning: I absorb ideas best by listening to verbal explanations, lectures, audio podcasts, and group debate discussions.",
                studentScenario = "Hearing a teacher explain a concept out loud or debating it with a study buddy helps it click effortlessly.",
                section = "LEARNING_STYLE",
                dimensionKey = "AUDITORY",
                dimensionTitle = "Learning Style: Auditory",
                trait = PersonalityTrait.SOCIAL,
                sortOrder = 22
            ),
            AssessmentQuestion(
                questionText = "Read & Write Learning: I comprehend information best through reading comprehensive text, taking detailed notes, and writing essays.",
                studentScenario = "Re-reading textbooks, annotating margins, and creating written summaries is your primary study strategy.",
                section = "LEARNING_STYLE",
                dimensionKey = "READ_WRITE",
                dimensionTitle = "Learning Style: Read & Write",
                trait = PersonalityTrait.CONVENTIONAL,
                sortOrder = 23
            ),
            AssessmentQuestion(
                questionText = "Kinesthetic Learning: I learn most thoroughly through physical practice, laboratory experiments, building prototypes, and hands-on trial-and-error.",
                studentScenario = "You remember a scientific principle or design formula best after actually doing it yourself in a lab.",
                section = "LEARNING_STYLE",
                dimensionKey = "KINESTHETIC",
                dimensionTitle = "Learning Style: Kinesthetic",
                trait = PersonalityTrait.REALISTIC,
                sortOrder = 24
            ),

            // Section 6: SKILLS & ABILITIES (8 Core Competencies)
            AssessmentQuestion(
                questionText = "Numerical Ability: I feel confident analyzing numerical formulas, calculating statistics, and interpreting quantitative data accurately.",
                studentScenario = "Working through algebra problems or calculating numerical budgets feels manageable and straightforward.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "NUMERICAL",
                dimensionTitle = "Skill: Numerical Ability",
                trait = PersonalityTrait.CONVENTIONAL,
                sortOrder = 25
            ),
            AssessmentQuestion(
                questionText = "Logical Reasoning: I quickly spot patterns, identify cause-and-effect relationships, and deduce rational solutions to complex logic challenges.",
                studentScenario = "You easily solve brain-teasers, algorithmic flow puzzles, or identify flaws in an inconsistent argument.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "LOGICAL",
                dimensionTitle = "Skill: Logical Reasoning",
                trait = PersonalityTrait.INVESTIGATIVE,
                sortOrder = 26
            ),
            AssessmentQuestion(
                questionText = "Verbal & Communication Fluency: I express concepts with rich vocabulary, persuasive clarity, and compelling written and oral eloquence.",
                studentScenario = "Writing essays, delivering speeches, or articulating intricate nuances comes very naturally to you.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "VERBAL",
                dimensionTitle = "Skill: Verbal Fluency",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 27
            ),
            AssessmentQuestion(
                questionText = "Administrative & Coordination Skills: I am proficient at scheduling milestones, coordinating logistics, managing documentation, and meeting deadlines.",
                studentScenario = "Organizing a school event or keeping track of detailed deadlines for a project is something you handle systematically.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "ADMINISTRATIVE",
                dimensionTitle = "Skill: Administrative Coordination",
                trait = PersonalityTrait.CONVENTIONAL,
                sortOrder = 28
            ),
            AssessmentQuestion(
                questionText = "Spatial & 3D Visualization: I can mentally manipulate 3D shapes, visualize spatial layouts, geometric transformations, and aesthetic compositions.",
                studentScenario = "When looking at a 2D architectural blueprint or design sketch, you can clearly envision how it looks in 3D physical reality.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "SPATIAL",
                dimensionTitle = "Skill: Spatial Visualization",
                trait = PersonalityTrait.ARTISTIC,
                sortOrder = 29
            ),
            AssessmentQuestion(
                questionText = "Leadership & Decision Making: I can make confident decisions under ambiguity, align team members, and navigate group responsibilities.",
                studentScenario = "When a group project stalls, you step in with composure to assign roles and drive the team forward.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "LEADERSHIP",
                dimensionTitle = "Skill: Leadership & Decisiveness",
                trait = PersonalityTrait.ENTERPRISING,
                sortOrder = 30
            ),
            AssessmentQuestion(
                questionText = "Social & Interpersonal Empathy: I naturally build trust, listen deeply without judgment, de-escalate tensions, and collaborate warmly.",
                studentScenario = "Classmates often confide in you because you understand how they feel and offer compassionate support.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "SOCIAL_COOPERATION",
                dimensionTitle = "Skill: Social Cooperation & Empathy",
                trait = PersonalityTrait.SOCIAL,
                sortOrder = 31
            ),
            AssessmentQuestion(
                questionText = "Mechanical & Physical Intuition: I understand how physical mechanisms, gears, tools, and mechanical forces interact in practical systems.",
                studentScenario = "You intuitively grasp how a pulley system, bicycle gear shift, or simple engine works mechanically.",
                section = "SKILLS_ABILITIES",
                dimensionKey = "MECHANICAL",
                dimensionTitle = "Skill: Mechanical Intuition",
                trait = PersonalityTrait.REALISTIC,
                sortOrder = 32
            )
        )
        questionDao.insertQuestions(questions)
    }

    private suspend fun seedProfilesAndHistory() {
        // Admin Faculty Profile
        val adminProfile = UserProfile(
            name = "Dr. Sarah Jenkins",
            email = "sjenkins@campus-advising.edu",
            role = UserRole.ADMIN,
            educationLevel = EducationLevel.COLLEGE_GRAD,
            academicMajor = "Counseling Psychology (Ph.D.)",
            targetIndustries = "Academic Advising, Career Development, Student Affairs",
            departmentalTitle = "Director of Career Counseling & Academic Advising",
            avatarColorHex = 0xFF7C3AED
        )
        val adminId = userDao.insertProfile(adminProfile)

        // Student Profile 1: Siya Paresh Solanki (CBC 26-Page Assessment Profile)
        val siyaProfile = UserProfile(
            name = "Siya Paresh Solanki",
            email = "careerbychoicesurat126@gmail.com",
            role = UserRole.STUDENT,
            educationLevel = EducationLevel.HIGH_SCHOOL_FRESHMAN,
            academicMajor = "Secondary School (10th Grade)",
            targetIndustries = "Creative Arts & Design, Human Services, Architecture, Media",
            assignedCounselor = "Dr. Sarah Jenkins",
            avatarColorHex = 0xFF8B5CF6
        )
        val siyaId = userDao.insertProfile(siyaProfile)

        // Student Profile 2: Jordan Miller (High School Senior)
        val jordanProfile = UserProfile(
            name = "Jordan Miller",
            email = "jordan.miller@highschool.edu",
            role = UserRole.STUDENT,
            educationLevel = EducationLevel.HIGH_SCHOOL_SENIOR,
            academicMajor = "Undecided / STEM & Design Interest",
            targetIndustries = "Technology, Interactive Media, Robotics",
            assignedCounselor = "Dr. Sarah Jenkins",
            avatarColorHex = 0xFF2563EB
        )
        val jordanId = userDao.insertProfile(jordanProfile)

        // Student Profile 3: Aaliyah Davis (College Sophomore)
        val aaliyahProfile = UserProfile(
            name = "Aaliyah Davis",
            email = "aaliyah.davis@university.edu",
            role = UserRole.STUDENT,
            educationLevel = EducationLevel.COLLEGE_UNDERGRAD,
            academicMajor = "Biochemistry & Public Health",
            targetIndustries = "Healthcare, Biomedical Sciences, Clinical Advising",
            assignedCounselor = "Dr. Sarah Jenkins",
            avatarColorHex = 0xFF10B981
        )
        val aaliyahId = userDao.insertProfile(aaliyahProfile)

        // Historical Assessment Result for Siya Paresh Solanki (Matches 26-page CBC Report 1:1)
        val siyaResult = AssessmentResult(
            studentId = siyaId,
            studentName = "Siya Paresh Solanki",
            studentEmail = "careerbychoicesurat126@gmail.com",
            educationLevel = EducationLevel.HIGH_SCHOOL_FRESHMAN,
            timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 2,
            realisticScore = 48,
            investigativeScore = 60,
            artisticScore = 91,
            socialScore = 69,
            enterprisingScore = 20,
            conventionalScore = 26,
            primaryTrait = PersonalityTrait.ARTISTIC,
            secondaryTrait = PersonalityTrait.SOCIAL,
            hollandCode = "ASI",
            planningStage = "Confused",
            planningStageRisk = "Wrong selection of a career path, career dissatisfaction, and mismatch with your interests.",
            planningStageActionPlan = "Explore your strengths and weaknesses > Explore career options > Gather information > Match the most suitable option > Early execution.",
            personalityType = "INFP",
            personalityTypeFullName = "Introvert : iNtuitive : Feeling : Perceiving (Spontaneous)",
            introvertScore = 71,
            extrovertScore = 29,
            intuitiveScore = 71,
            sensingScore = 29,
            feelingScore = 57,
            thinkingScore = 43,
            perceivingScore = 71,
            judgingScore = 29,
            motivatorCreativity = 100,
            motivatorIndependence = 100,
            motivatorAdventure = 60,
            motivatorSocialService = 60,
            motivatorContinuousLearning = 60,
            motivatorStructuredEnvironment = 60,
            motivatorHighPaced = 40,
            learningStyleVisual = 25,
            learningStyleAuditory = 25,
            learningStyleReadWrite = 25,
            learningStyleKinesthetic = 25,
            skillNumerical = 20,
            skillLogical = 40,
            skillVerbal = 100,
            skillAdministrative = 30,
            skillSpatial = 80,
            skillLeadership = 48,
            skillSocialCooperation = 40,
            skillMechanical = 50,
            overallSkillsAverage = 51,
            selectedClustersSummary = "Creative Arts & Design, Human Services, Architecture and Construction, Media and Communication",
            humanitiesMatch = 93,
            scienceMathsMatch = 61,
            scienceBioMatch = 39,
            commerceMatch = 36,
            topCareersSummary = "Fine Arts – Drawing and Painting (Top Choice), Interior Design (Top Choice), Photography (Top Choice), Graphic Design (Top Choice)",
            careerReadinessScore = 88,
            counselorNotes = "Siya demonstrates remarkable creative, verbal, and spatial gifts with an INFP archetype. Highly suited for Humanities or Creative Design streams. Recommend exploring Fine Arts, Graphic/UX Design, and Architecture.",
            counselorReviewed = true,
            counselorReviewedBy = "Dr. Sarah Jenkins",
            counselorReviewedTimestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 1
        )
        assessmentDao.insertResult(siyaResult)

        // Historical Assessment Results for Jordan Miller
        val pastResultJordan1 = AssessmentResult(
            studentId = jordanId,
            studentName = "Jordan Miller",
            studentEmail = "jordan.miller@highschool.edu",
            educationLevel = EducationLevel.HIGH_SCHOOL_SENIOR,
            timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 35, // 35 days ago
            realisticScore = 72,
            investigativeScore = 88,
            artisticScore = 80,
            socialScore = 45,
            enterprisingScore = 58,
            conventionalScore = 60,
            primaryTrait = PersonalityTrait.INVESTIGATIVE,
            secondaryTrait = PersonalityTrait.ARTISTIC,
            hollandCode = "IAR",
            planningStage = "Exploring",
            personalityType = "INTJ",
            personalityTypeFullName = "Introvert : iNtuitive : Thinking : Judging",
            topCareersSummary = "Software & AI Solutions Architect, Digital Product Design, Robotics Engineer",
            careerReadinessScore = 82,
            counselorNotes = "Initial baseline test. Jordan exhibits high investigative problem solving coupled with strong visual design instincts. Recommended taking AP Computer Science and preparing a digital design portfolio.",
            counselorReviewed = true,
            counselorReviewedBy = "Dr. Sarah Jenkins",
            counselorReviewedTimestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30
        )
        assessmentDao.insertResult(pastResultJordan1)

        val pastResultJordan2 = AssessmentResult(
            studentId = jordanId,
            studentName = "Jordan Miller",
            studentEmail = "jordan.miller@highschool.edu",
            educationLevel = EducationLevel.HIGH_SCHOOL_SENIOR,
            timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5, // 5 days ago
            realisticScore = 76,
            investigativeScore = 92,
            artisticScore = 85,
            socialScore = 50,
            enterprisingScore = 65,
            conventionalScore = 58,
            primaryTrait = PersonalityTrait.INVESTIGATIVE,
            secondaryTrait = PersonalityTrait.ARTISTIC,
            hollandCode = "IAE",
            planningStage = "Clarity",
            personalityType = "INTP",
            personalityTypeFullName = "Introvert : iNtuitive : Thinking : Perceiving",
            topCareersSummary = "Software & AI Solutions Architect, Web Design UI/UX, Product Design",
            careerReadinessScore = 90,
            counselorNotes = "Follow-up test shows increased entrepreneurial focus and sharpened technical clarity. Excellent candidate for Human-Computer Interaction or Dual CS/Design collegiate programs.",
            counselorReviewed = true,
            counselorReviewedBy = "Dr. Sarah Jenkins",
            counselorReviewedTimestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 3
        )
        assessmentDao.insertResult(pastResultJordan2)

        // Historical Assessment Result for Aaliyah Davis
        val aaliyahResult = AssessmentResult(
            studentId = aaliyahId,
            studentName = "Aaliyah Davis",
            studentEmail = "aaliyah.davis@university.edu",
            educationLevel = EducationLevel.COLLEGE_UNDERGRAD,
            timestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 12,
            realisticScore = 48,
            investigativeScore = 94,
            artisticScore = 40,
            socialScore = 88,
            enterprisingScore = 52,
            conventionalScore = 68,
            primaryTrait = PersonalityTrait.INVESTIGATIVE,
            secondaryTrait = PersonalityTrait.SOCIAL,
            hollandCode = "ISC",
            planningStage = "Clarity",
            personalityType = "INFJ",
            personalityTypeFullName = "Introvert : iNtuitive : Feeling : Judging",
            topCareersSummary = "Clinical Psychology, Counselling Psychology, Sociology",
            careerReadinessScore = 94,
            counselorNotes = "Aaliyah combines rigorous analytical research with profound empathetic drive. High suitability for clinical biomedical trials or pre-med paths.",
            counselorReviewed = true,
            counselorReviewedBy = "Dr. Sarah Jenkins",
            counselorReviewedTimestamp = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 10
        )
        assessmentDao.insertResult(aaliyahResult)

        // Counseling Appointments
        val appointment1 = CounselingAppointment(
            studentId = siyaId,
            studentName = "Siya Paresh Solanki",
            studentEmail = "careerbychoicesurat126@gmail.com",
            counselorName = "Dr. Sarah Jenkins",
            appointmentType = "26-Page Comprehensive Psychometric Career Counseling Debrief",
            timestamp = System.currentTimeMillis() + 1000L * 60 * 60 * 24, // In 24 hours
            durationMinutes = 60,
            location = "Academic Counseling Center, Suite 101",
            status = "SCHEDULED",
            notes = "Discuss 10th-grade stream selection: Humanities vs Science-Maths. Review top 4 career clusters (Creative Arts, Human Services, Architecture, Media)."
        )
        appointmentDao.insertAppointment(appointment1)

        val appointment2 = CounselingAppointment(
            studentId = jordanId,
            studentName = "Jordan Miller",
            studentEmail = "jordan.miller@highschool.edu",
            counselorName = "Dr. Sarah Jenkins",
            appointmentType = "Post-Assessment Career Roadmap & College Major Debrief",
            timestamp = System.currentTimeMillis() + 1000L * 60 * 60 * 48,
            durationMinutes = 45,
            location = "Academic Counseling Center, Suite 210",
            status = "SCHEDULED",
            notes = "Review recent assessment shift toward HCI & AI Architecture. Finalize university applications."
        )
        appointmentDao.insertAppointment(appointment2)

        // Assessment Deadlines
        val deadline1 = AssessmentDeadline(
            title = "Secondary & High School Stream Selection Deadline",
            targetAudience = "8th, 9th & 10th Grade Students",
            dueDate = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 5, // In 5 days
            description = "Mandatory career counseling assessment completion to finalize academic stream recommendations (Humanities, Science, Commerce).",
            isUrgent = true
        )
        appointmentDao.insertDeadline(deadline1)

        val deadline2 = AssessmentDeadline(
            title = "Spring Internship & Industry Match Roster Update",
            targetAudience = "All Undergraduates & High School Seniors",
            dueDate = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 18,
            description = "Update career goals and download updated PDF reports for faculty endorsement before campus career fair.",
            isUrgent = false
        )
        appointmentDao.insertDeadline(deadline2)
    }

    // Scoring & Career Matching Logic
    suspend fun submitAssessment(
        student: UserProfile,
        answers: Map<Long, Int> // questionId -> rating (1 to 5)
    ): AssessmentResult = withContext(Dispatchers.IO) {
        val questions = questionDao.getAllQuestions().first()
        val questionsById = questions.associateBy { it.id }

        fun ratingToPercent(rating: Int): Int = ((rating - 1) * 25).coerceIn(10, 100)

        // 1. Planning Stage
        val stageAnswers = answers.filter { questionsById[it.key]?.section == "PLANNING_STAGE" }
        val avgStageRating = if (stageAnswers.isNotEmpty()) stageAnswers.values.average() else 3.8
        val (stageName, stageRisk, stageAction) = when {
            avgStageRating >= 3.8 -> Triple(
                "Confused",
                "Wrong selection of a career path, career dissatisfaction, and mismatch with your interests.",
                "Explore your strengths and weaknesses > Explore career options > Gather information > Match the most suitable option > Early execution."
            )
            avgStageRating >= 2.8 -> Triple(
                "Exploring",
                "Information overload, getting distracted by non-core career options, and delays in selecting electives.",
                "Narrow down top 3 interest fields > Conduct informational interviews with professionals > Build focused skillsets."
            )
            avgStageRating >= 2.0 -> Triple(
                "Clarity",
                "Overconfidence without testing assumptions in real projects or internships.",
                "Deepen practical competencies > Secure internships > Refine academic subject choices."
            )
            else -> Triple(
                "Future-Ready",
                "Burnout or lack of flexibility when market industry trends evolve.",
                "Maintain agility > Expand professional network > Execute milestone roadmap."
            )
        }

        // 2. Career Personality (MBTI)
        val eiRating = answers.entries.find { questionsById[it.key]?.dimensionKey == "EI" }?.value ?: 4
        val introvertScore = ((eiRating - 1) * 20 + 20).coerceIn(20, 95)
        val extrovertScore = 100 - introvertScore

        val snRating = answers.entries.find { questionsById[it.key]?.dimensionKey == "SN" }?.value ?: 4
        val intuitiveScore = ((snRating - 1) * 20 + 20).coerceIn(20, 95)
        val sensingScore = 100 - intuitiveScore

        val tfRating = answers.entries.find { questionsById[it.key]?.dimensionKey == "TF" }?.value ?: 3
        val feelingScore = ((tfRating - 1) * 20 + 20).coerceIn(20, 95)
        val thinkingScore = 100 - feelingScore

        val jpRating = answers.entries.find { questionsById[it.key]?.dimensionKey == "JP" }?.value ?: 4
        val perceivingScore = ((jpRating - 1) * 20 + 20).coerceIn(20, 95)
        val judgingScore = 100 - perceivingScore

        val mbtiFirst = if (introvertScore >= 50) "I" else "E"
        val mbtiSecond = if (intuitiveScore >= 50) "N" else "S"
        val mbtiThird = if (feelingScore >= 50) "F" else "T"
        val mbtiFourth = if (perceivingScore >= 50) "P" else "J"
        val personalityType = "$mbtiFirst$mbtiSecond$mbtiThird$mbtiFourth"

        val personalityTypeFullName = "${if (mbtiFirst == "I") "Introvert" else "Extrovert"} : ${if (mbtiSecond == "N") "iNtuitive" else "Sensing"} : ${if (mbtiThird == "F") "Feeling" else "Thinking"} : ${if (mbtiFourth == "P") "Perceiving (Spontaneous)" else "Judging (Structured)"}"

        // 3. Career Interest (RIASEC)
        fun getInterestScore(code: String): Int {
            val q = questions.find { it.section == "CAREER_INTEREST" && it.dimensionKey == code }
            val rating = if (q != null) answers[q.id] ?: 3 else 3
            return ratingToPercent(rating)
        }
        val rScore = getInterestScore("R")
        val iScore = getInterestScore("I")
        val aScore = getInterestScore("A")
        val sScore = getInterestScore("S")
        val eScore = getInterestScore("E")
        val cScore = getInterestScore("C")

        val scoresList = listOf(
            PersonalityTrait.ARTISTIC to aScore,
            PersonalityTrait.SOCIAL to sScore,
            PersonalityTrait.INVESTIGATIVE to iScore,
            PersonalityTrait.REALISTIC to rScore,
            PersonalityTrait.CONVENTIONAL to cScore,
            PersonalityTrait.ENTERPRISING to eScore
        ).sortedByDescending { it.second }

        val primaryTrait = scoresList[0].first
        val secondaryTrait = scoresList[1].first
        val tertiaryTrait = scoresList[2].first
        val hollandCode = "${primaryTrait.code}${secondaryTrait.code}${tertiaryTrait.code}"

        // 4. Career Motivators
        fun getMotivatorScore(key: String): Int {
            val q = questions.find { it.section == "CAREER_MOTIVATOR" && it.dimensionKey == key }
            val rating = if (q != null) answers[q.id] ?: 3 else 3
            return ratingToPercent(rating)
        }
        val motCreativity = getMotivatorScore("CREATIVITY")
        val motIndependence = getMotivatorScore("INDEPENDENCE")
        val motAdventure = getMotivatorScore("ADVENTURE")
        val motSocialService = getMotivatorScore("SOCIAL_SERVICE")
        val motContinuousLearning = getMotivatorScore("CONTINUOUS_LEARNING")
        val motStructured = getMotivatorScore("STRUCTURED_ENV")
        val motHighPaced = getMotivatorScore("HIGH_PACED")

        // 5. Learning Styles (VARK)
        fun getLearningScore(key: String): Int {
            val q = questions.find { it.section == "LEARNING_STYLE" && it.dimensionKey == key }
            val rating = if (q != null) answers[q.id] ?: 3 else 3
            return ratingToPercent(rating)
        }
        val learnVisual = getLearningScore("VISUAL")
        val learnAuditory = getLearningScore("AUDITORY")
        val learnReadWrite = getLearningScore("READ_WRITE")
        val learnKinesthetic = getLearningScore("KINESTHETIC")
        val totalLearning = (learnVisual + learnAuditory + learnReadWrite + learnKinesthetic).coerceAtLeast(1)
        val lVisualPct = (learnVisual * 100 / totalLearning).coerceIn(15, 45)
        val lAuditoryPct = (learnAuditory * 100 / totalLearning).coerceIn(15, 45)
        val lReadPct = (learnReadWrite * 100 / totalLearning).coerceIn(15, 45)
        val lKinestheticPct = (100 - lVisualPct - lAuditoryPct - lReadPct).coerceIn(15, 45)

        // 6. Skills & Abilities
        fun getSkillScore(key: String): Int {
            val q = questions.find { it.section == "SKILLS_ABILITIES" && it.dimensionKey == key }
            val rating = if (q != null) answers[q.id] ?: 3 else 3
            return ratingToPercent(rating)
        }
        val skNumerical = getSkillScore("NUMERICAL")
        val skLogical = getSkillScore("LOGICAL")
        val skVerbal = getSkillScore("VERBAL")
        val skAdmin = getSkillScore("ADMINISTRATIVE")
        val skSpatial = getSkillScore("SPATIAL")
        val skLeadership = getSkillScore("LEADERSHIP")
        val skSocial = getSkillScore("SOCIAL_COOPERATION")
        val skMechanical = getSkillScore("MECHANICAL")
        val overallSkillAvg = ((skNumerical + skLogical + skVerbal + skAdmin + skSpatial + skLeadership + skSocial + skMechanical) / 8)

        // 7. Academic Stream Matches
        val humanities = ((aScore * 0.45f) + (sScore * 0.35f) + (skVerbal * 0.20f)).toInt().coerceIn(40, 98)
        val scienceMaths = ((iScore * 0.40f) + (skLogical * 0.35f) + (skNumerical * 0.25f)).toInt().coerceIn(30, 95)
        val scienceBio = ((iScore * 0.45f) + (sScore * 0.30f) + (skLogical * 0.25f)).toInt().coerceIn(30, 95)
        val commerce = ((eScore * 0.40f) + (cScore * 0.35f) + (skNumerical * 0.25f)).toInt().coerceIn(30, 95)

        // 8. Careers Match
        val allCareersList = careerDao.getAllCareers().first()
        val scoredCareers = allCareersList.map { career ->
            val pScore = when (career.primaryTrait) {
                PersonalityTrait.REALISTIC -> rScore
                PersonalityTrait.INVESTIGATIVE -> iScore
                PersonalityTrait.ARTISTIC -> aScore
                PersonalityTrait.SOCIAL -> sScore
                PersonalityTrait.ENTERPRISING -> eScore
                PersonalityTrait.CONVENTIONAL -> cScore
            }
            val sScoreVal = when (career.secondaryTrait) {
                PersonalityTrait.REALISTIC -> rScore
                PersonalityTrait.INVESTIGATIVE -> iScore
                PersonalityTrait.ARTISTIC -> aScore
                PersonalityTrait.SOCIAL -> sScore
                PersonalityTrait.ENTERPRISING -> eScore
                PersonalityTrait.CONVENTIONAL -> cScore
            }
            val matchScore = (pScore * 0.65f + sScoreVal * 0.35f).toInt()
            career to matchScore
        }.sortedByDescending { it.second }

        val top3Titles = scoredCareers.take(3).joinToString(", ") { "${it.first.title} (${it.first.recommendationComment})" }
        val readiness = ((scoresList.take(3).map { it.second }.average() * 0.5) + (overallSkillAvg * 0.5)).toInt().coerceIn(60, 98)

        val assessmentResult = AssessmentResult(
            studentId = student.id,
            studentName = student.name,
            studentEmail = student.email,
            educationLevel = student.educationLevel,
            timestamp = System.currentTimeMillis(),
            realisticScore = rScore,
            investigativeScore = iScore,
            artisticScore = aScore,
            socialScore = sScore,
            enterprisingScore = eScore,
            conventionalScore = cScore,
            primaryTrait = primaryTrait,
            secondaryTrait = secondaryTrait,
            hollandCode = hollandCode,
            planningStage = stageName,
            planningStageRisk = stageRisk,
            planningStageActionPlan = stageAction,
            personalityType = personalityType,
            personalityTypeFullName = personalityTypeFullName,
            introvertScore = introvertScore,
            extrovertScore = extrovertScore,
            intuitiveScore = intuitiveScore,
            sensingScore = sensingScore,
            feelingScore = feelingScore,
            thinkingScore = thinkingScore,
            perceivingScore = perceivingScore,
            judgingScore = judgingScore,
            motivatorCreativity = motCreativity,
            motivatorIndependence = motIndependence,
            motivatorAdventure = motAdventure,
            motivatorSocialService = motSocialService,
            motivatorContinuousLearning = motContinuousLearning,
            motivatorStructuredEnvironment = motStructured,
            motivatorHighPaced = motHighPaced,
            learningStyleVisual = lVisualPct,
            learningStyleAuditory = lAuditoryPct,
            learningStyleReadWrite = lReadPct,
            learningStyleKinesthetic = lKinestheticPct,
            skillNumerical = skNumerical,
            skillLogical = skLogical,
            skillVerbal = skVerbal,
            skillAdministrative = skAdmin,
            skillSpatial = skSpatial,
            skillLeadership = skLeadership,
            skillSocialCooperation = skSocial,
            skillMechanical = skMechanical,
            overallSkillsAverage = overallSkillAvg,
            selectedClustersSummary = "Creative Arts & Design, Human Services, Architecture and Construction, Media and Communication",
            humanitiesMatch = humanities,
            scienceMathsMatch = scienceMaths,
            scienceBioMatch = scienceBio,
            commerceMatch = commerce,
            topCareersSummary = top3Titles,
            careerReadinessScore = readiness,
            counselorNotes = "Assessment scored successfully. Primary trait ${primaryTrait.title} (${aScore}%) aligns strongly with ${personalityType} profile. Top academic stream: Humanities (${humanities}%).",
            counselorReviewed = false
        )

        val newId = assessmentDao.insertResult(assessmentResult)
        assessmentResult.copy(id = newId)
    }

    suspend fun addCounselorReview(
        resultId: Long,
        notes: String,
        counselorName: String
    ) = withContext(Dispatchers.IO) {
        assessmentDao.addCounselorReview(
            id = resultId,
            notes = notes,
            counselorName = counselorName,
            timestamp = System.currentTimeMillis()
        )
    }

    suspend fun saveCareer(career: CareerPath) = withContext(Dispatchers.IO) {
        if (career.id == 0L) {
            careerDao.insertCareer(career.copy(isCustomAdded = true))
        } else {
            careerDao.updateCareer(career)
        }
    }

    suspend fun deleteCareer(id: Long) = withContext(Dispatchers.IO) {
        careerDao.deleteCareer(id)
    }

    suspend fun saveQuestion(question: AssessmentQuestion) = withContext(Dispatchers.IO) {
        if (question.id == 0L) {
            questionDao.insertQuestion(question)
        } else {
            questionDao.updateQuestion(question)
        }
    }

    suspend fun deleteQuestion(id: Long) = withContext(Dispatchers.IO) {
        questionDao.deleteQuestion(id)
    }

    suspend fun scheduleAppointment(appointment: CounselingAppointment): Long = withContext(Dispatchers.IO) {
        appointmentDao.insertAppointment(appointment)
    }

    suspend fun updateAppointmentStatus(id: Long, status: String) = withContext(Dispatchers.IO) {
        appointmentDao.updateAppointmentStatus(id, status)
    }

    suspend fun markAppointmentReminderSent(id: Long) = withContext(Dispatchers.IO) {
        appointmentDao.markReminderSent(id)
    }

    suspend fun saveDeadline(deadline: AssessmentDeadline): Long = withContext(Dispatchers.IO) {
        if (deadline.id == 0L) {
            appointmentDao.insertDeadline(deadline)
        } else {
            appointmentDao.updateDeadline(deadline)
            deadline.id
        }
    }

    suspend fun markDeadlineNotified(id: Long) = withContext(Dispatchers.IO) {
        appointmentDao.markDeadlineNotified(id)
    }

    suspend fun deleteDeadline(id: Long) = withContext(Dispatchers.IO) {
        appointmentDao.deleteDeadline(id)
    }
}
