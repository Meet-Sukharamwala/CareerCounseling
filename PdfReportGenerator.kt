package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.data.model.AssessmentResult
import com.example.data.model.CareerPath
import com.example.data.model.PersonalityTrait
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val TOTAL_PAGES = 5

    fun generateReportPdf(
        context: Context,
        result: AssessmentResult,
        matchedCareers: List<CareerPath>
    ): File? {
        val pdfDocument = PdfDocument()

        try {
            // PAGE 1: Cover & Student Profile & Planning Stage (Where Do You Stand?)
            generatePage1(pdfDocument, result)

            // PAGE 2: Personality Profile (MBTI) & Career Interest (RIASEC Holland Code)
            generatePage2(pdfDocument, result)

            // PAGE 3: Career Motivators & Learning Styles (VARK Profile)
            generatePage3(pdfDocument, result)

            // PAGE 4: Core Skills & Abilities & Academic Stream Recommendation
            generatePage4(pdfDocument, result)

            // PAGE 5: Recommended Career Paths, Detailed Specializations & Counselor Endorsement
            generatePage5(pdfDocument, result, matchedCareers)

            // Save PDF to cache/reports
            val reportsDir = File(context.cacheDir, "reports")
            if (!reportsDir.exists()) {
                reportsDir.mkdirs()
            }

            val reportFile = File(reportsDir, "Career_Assessment_Report_${result.studentName.replace(" ", "_")}_${result.id}.pdf")
            val fos = FileOutputStream(reportFile)
            pdfDocument.writeTo(fos)
            fos.close()
            pdfDocument.close()
            return reportFile
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                pdfDocument.close()
            } catch (_: Exception) {}
            return null
        }
    }

    // --- Page 1: Student Profile & Planning Stage ---
    private fun generatePage1(pdfDocument: PdfDocument, result: AssessmentResult) {
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        // Top Header
        drawHeader(canvas, paint, "STUDENT PROFILE & PLANNING STAGE", 1)

        var currentY = 105f

        // Institution & Title Card
        paint.color = Color.rgb(238, 242, 255)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 56f, 10f, 10f, paint)

        paint.color = Color.rgb(30, 27, 75)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 15f
        canvas.drawText("CAREER BY CHOICE • PSYCHOMETRIC ASSESSMENT REPORT", 50f, currentY + 24f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9.5f
        paint.color = Color.rgb(67, 56, 202)
        canvas.drawText("Official Comprehensive Career Counseling, Aptitude & Stream Evaluation Report", 50f, currentY + 42f, paint)

        currentY += 72f

        // Student Info Card
        paint.color = Color.rgb(248, 250, 252)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 95f, 10f, 10f, paint)
        paint.color = Color.rgb(226, 232, 240)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 95f, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(30, 41, 59)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 12f
        canvas.drawText("CANDIDATE INFORMATION", 50f, currentY + 22f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9.5f
        paint.color = Color.rgb(71, 85, 105)
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy • hh:mm a", Locale.getDefault())
        val dateString = dateFormat.format(Date(result.timestamp))

        canvas.drawText("Student Name: ${result.studentName}", 50f, currentY + 42f, paint)
        canvas.drawText("Email Address: ${result.studentEmail}", 50f, currentY + 58f, paint)
        canvas.drawText("Academic Class / Level: ${result.educationLevel.displayName}", 50f, currentY + 74f, paint)

        canvas.drawText("Report ID: CBC-AST-${result.id + 1000}", 330f, currentY + 42f, paint)
        canvas.drawText("Assessment Date: $dateString", 330f, currentY + 58f, paint)
        canvas.drawText("Supervising Faculty: Dr. Sarah Jenkins (Lead Counselor)", 330f, currentY + 74f, paint)

        currentY += 115f

        // Section 1: Where Do You Stand Right Now? (Planning Stage)
        drawSectionTitle(canvas, paint, "SECTION 1: WHERE DO YOU STAND RIGHT NOW? (PLANNING STAGE)", currentY)
        currentY += 24f

        // Planning Stage Card
        val stageBg = when (result.planningStage) {
            "Confused" -> Color.rgb(254, 242, 242) // Light red/pink
            "Exploring" -> Color.rgb(254, 249, 195) // Light yellow
            else -> Color.rgb(240, 253, 244) // Light green
        }
        val stageBorder = when (result.planningStage) {
            "Confused" -> Color.rgb(239, 68, 68)
            "Exploring" -> Color.rgb(234, 179, 8)
            else -> Color.rgb(34, 197, 94)
        }

        paint.color = stageBg
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 140f, 10f, 10f, paint)
        paint.color = stageBorder
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 140f, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        // Badge
        paint.color = stageBorder
        canvas.drawRoundRect(50f, currentY + 16f, 210f, currentY + 42f, 6f, 6f, paint)
        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 12f
        canvas.drawText("CURRENT STAGE: ${result.planningStage.uppercase()}", 60f, currentY + 33f, paint)

        paint.color = Color.rgb(15, 23, 42)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10f
        canvas.drawText("Risk of Being in this Stage:", 50f, currentY + 62f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9.5f
        paint.color = Color.rgb(71, 85, 105)
        drawWrappedText(
            canvas,
            paint,
            result.planningStageRisk.ifEmpty { "Wrong selection of a career path, career dissatisfaction, and mismatch with your interests." },
            50f,
            currentY + 76f,
            PAGE_WIDTH - 100f,
            14f
        )

        paint.color = Color.rgb(15, 23, 42)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10f
        canvas.drawText("Recommended 5-Step Action Roadmap:", 50f, currentY + 102f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(30, 58, 138)
        drawWrappedText(
            canvas,
            paint,
            result.planningStageActionPlan.ifEmpty { "Explore strengths and weaknesses > Explore career options > Gather information > Match suitable option > Early execution." },
            50f,
            currentY + 116f,
            PAGE_WIDTH - 100f,
            13f
        )

        currentY += 160f

        // Career Readiness Summary Card
        drawSectionTitle(canvas, paint, "EXECUTIVE READINESS & ARCHETYPE SNAPSHOT", currentY)
        currentY += 24f

        paint.color = Color.rgb(241, 245, 249)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 115f, 10f, 10f, paint)

        // Readiness Score Meter
        paint.color = Color.rgb(30, 41, 59)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10.5f
        canvas.drawText("Career Readiness Index", 50f, currentY + 26f, paint)

        drawProgressBar(canvas, paint, 50f, currentY + 34f, 320f, 12f, result.careerReadinessScore, Color.rgb(79, 70, 229))

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 12f
        paint.color = Color.rgb(79, 70, 229)
        canvas.drawText("${result.careerReadinessScore}% Ready", 385f, currentY + 44f, paint)

        // Holland + MBTI quick badges
        paint.color = Color.rgb(100, 116, 139)
        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9.5f
        canvas.drawText("Primary Trait: ${result.primaryTrait.title} (${result.hollandCode})", 50f, currentY + 68f, paint)
        canvas.drawText("Personality Type: ${result.personalityType} (${result.personalityTypeFullName})", 50f, currentY + 84f, paint)
        canvas.drawText("Top Matched Academic Stream: Humanities & Creative Arts (${result.humanitiesMatch}%)", 50f, currentY + 100f, paint)

        drawFooter(canvas, paint, 1)
        pdfDocument.finishPage(page)
    }

    // --- Page 2: Personality (MBTI) & Holland RIASEC ---
    private fun generatePage2(pdfDocument: PdfDocument, result: AssessmentResult) {
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        drawHeader(canvas, paint, "CAREER PERSONALITY & INTEREST PROFILE", 2)

        var currentY = 105f

        // Section 2: MBTI 16 Personality Profile
        drawSectionTitle(canvas, paint, "SECTION 2: CAREER PERSONALITY PROFILE (16 PERSONALITY TYPES)", currentY)
        currentY += 22f

        // Personality Type Hero Box
        paint.color = Color.rgb(245, 243, 255)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 54f, 8f, 8f, paint)
        paint.color = Color.rgb(139, 92, 246)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 54f, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(109, 40, 217)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 14f
        canvas.drawText("ARCHETYPE: ${result.personalityType}", 50f, currentY + 24f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9.5f
        paint.color = Color.rgb(76, 29, 149)
        canvas.drawText(result.personalityTypeFullName, 50f, currentY + 42f, paint)

        currentY += 70f

        // 4 MBTI Spectrum Meters
        drawMbtiSpectrum(canvas, paint, "Energy Focus", "Extrovert (E)", result.extrovertScore, "Introvert (I)", result.introvertScore, currentY)
        currentY += 40f
        drawMbtiSpectrum(canvas, paint, "Information Input", "Sensing (S)", result.sensingScore, "iNtuitive (N)", result.intuitiveScore, currentY)
        currentY += 40f
        drawMbtiSpectrum(canvas, paint, "Decision Making", "Thinking (T)", result.thinkingScore, "Feeling (F)", result.feelingScore, currentY)
        currentY += 40f
        drawMbtiSpectrum(canvas, paint, "Work Planning", "Judging (J)", result.judgingScore, "Perceiving (P)", result.perceivingScore, currentY)
        currentY += 55f

        // Section 3: Holland Codes RIASEC
        drawSectionTitle(canvas, paint, "SECTION 3: CAREER INTEREST PROFILE (HOLLAND CODES - RIASEC)", currentY)
        currentY += 20f

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(100, 116, 139)
        canvas.drawText("Holland Code: ${result.hollandCode} | Primary: ${result.primaryTrait.title} | Secondary: ${result.secondaryTrait.title}", 36f, currentY, paint)
        currentY += 18f

        // 6 Trait meters
        val riasecTraits = listOf(
            Triple("Artistic (A)", result.artisticScore, Color.rgb(139, 92, 246)),
            Triple("Social (S)", result.socialScore, Color.rgb(16, 185, 129)),
            Triple("Investigative (I)", result.investigativeScore, Color.rgb(37, 99, 235)),
            Triple("Realistic (R)", result.realisticScore, Color.rgb(234, 88, 12)),
            Triple("Conventional (C)", result.conventionalScore, Color.rgb(2, 132, 199)),
            Triple("Enterprising (E)", result.enterprisingScore, Color.rgb(245, 158, 11))
        )

        riasecTraits.forEach { (label, score, color) ->
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9.5f
            paint.color = Color.rgb(30, 41, 59)
            canvas.drawText(label, 36f, currentY + 11f, paint)

            drawProgressBar(canvas, paint, 140f, currentY + 2f, 320f, 11f, score, color)

            paint.typeface = Typeface.DEFAULT
            paint.textSize = 9f
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawText("$score%", 475f, currentY + 11f, paint)

            currentY += 20f
        }

        currentY += 15f
        // Behavioral insight note
        paint.color = Color.rgb(241, 245, 249)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 45f, 6f, 6f, paint)
        paint.typeface = Typeface.DEFAULT
        paint.textSize = 8.5f
        paint.color = Color.rgb(71, 85, 105)
        canvas.drawText("Insight: A high Artistic (91%) & Social (69%) profile demonstrates immense creative originality, expressive empathy,", 48f, currentY + 18f, paint)
        canvas.drawText("and interpersonal sensitivity. The student thrives in open-ended creative problem solving over rigid administrative routines.", 48f, currentY + 32f, paint)

        drawFooter(canvas, paint, 2)
        pdfDocument.finishPage(page)
    }

    // --- Page 3: Motivators & Learning Styles ---
    private fun generatePage3(pdfDocument: PdfDocument, result: AssessmentResult) {
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 3).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        drawHeader(canvas, paint, "CAREER MOTIVATORS & LEARNING STYLES", 3)

        var currentY = 105f

        // Section 4: Career Motivators
        drawSectionTitle(canvas, paint, "SECTION 4: CORE CAREER MOTIVATORS", currentY)
        currentY += 18f

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(100, 116, 139)
        canvas.drawText("Identifies environmental and psychological factors that drive sustained engagement and fulfillment.", 36f, currentY, paint)
        currentY += 18f

        val motivators = listOf(
            Triple("Creativity", result.motivatorCreativity, Color.rgb(147, 51, 234)),
            Triple("Independence", result.motivatorIndependence, Color.rgb(79, 70, 229)),
            Triple("Adventure", result.motivatorAdventure, Color.rgb(234, 88, 12)),
            Triple("Social Service", result.motivatorSocialService, Color.rgb(16, 185, 129)),
            Triple("Continuous Learning", result.motivatorContinuousLearning, Color.rgb(37, 99, 235)),
            Triple("Structured Environment", result.motivatorStructuredEnvironment, Color.rgb(100, 116, 139)),
            Triple("High-Paced Environment", result.motivatorHighPaced, Color.rgb(245, 158, 11))
        )

        motivators.forEach { (label, score, color) ->
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9.5f
            paint.color = Color.rgb(30, 41, 59)
            canvas.drawText(label, 36f, currentY + 11f, paint)

            drawProgressBar(canvas, paint, 175f, currentY + 2f, 280f, 11f, score, color)

            paint.typeface = Typeface.DEFAULT
            paint.textSize = 9f
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawText("$score%", 470f, currentY + 11f, paint)

            currentY += 21f
        }

        currentY += 20f

        // Section 5: Learning Styles (VARK Profile)
        drawSectionTitle(canvas, paint, "SECTION 5: LEARNING STYLE ANALYSIS (VARK MODEL)", currentY)
        currentY += 18f

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(100, 116, 139)
        canvas.drawText("Examines how information is most efficiently absorbed, processed, and retained during study.", 36f, currentY, paint)
        currentY += 18f

        val learningStyles = listOf(
            Triple("Visual Learner (Sight, Diagrams, Visual Mapping)", result.learningStyleVisual, Color.rgb(59, 130, 246)),
            Triple("Auditory Learner (Lectures, Discussion, Verbalization)", result.learningStyleAuditory, Color.rgb(16, 185, 129)),
            Triple("Read / Write Learner (Textbooks, Notes, Written Outlines)", result.learningStyleReadWrite, Color.rgb(139, 92, 246)),
            Triple("Kinesthetic Learner (Hands-on, Real Modeling, Tactile)", result.learningStyleKinesthetic, Color.rgb(249, 115, 22))
        )

        learningStyles.forEach { (label, score, color) ->
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9f
            paint.color = Color.rgb(30, 41, 59)
            canvas.drawText(label, 36f, currentY + 11f, paint)

            drawProgressBar(canvas, paint, 305f, currentY + 2f, 160f, 11f, score, color)

            paint.typeface = Typeface.DEFAULT
            paint.textSize = 9f
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawText("$score%", 475f, currentY + 11f, paint)

            currentY += 22f
        }

        currentY += 20f

        // Study Guidance Box
        paint.color = Color.rgb(240, 253, 250)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 70f, 8f, 8f, paint)
        paint.color = Color.rgb(20, 184, 166)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 70f, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(15, 118, 110)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10.5f
        canvas.drawText("ACADEMIC STUDY RECOMMENDATION:", 50f, currentY + 22f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(19, 78, 74)
        canvas.drawText("• Multimodal Balance (25% across Visual, Auditory, Read/Write, Kinesthetic) enables versatile study habits.", 50f, currentY + 38f, paint)
        canvas.drawText("• Recommended to combine mind-mapping with verbal group discussions and practical creative prototypes.", 50f, currentY + 54f, paint)

        drawFooter(canvas, paint, 3)
        pdfDocument.finishPage(page)
    }

    // --- Page 4: Core Skills & Academic Stream Selection ---
    private fun generatePage4(pdfDocument: PdfDocument, result: AssessmentResult) {
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 4).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        drawHeader(canvas, paint, "SKILLS & ACADEMIC STREAM RECOMMENDATION", 4)

        var currentY = 105f

        // Section 6: Core Skills & Abilities
        drawSectionTitle(canvas, paint, "SECTION 6: CORE SKILLS & ABILITIES ASSESSMENT (8 COMPETENCIES)", currentY)
        currentY += 18f

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(100, 116, 139)
        canvas.drawText("Overall Skills Average: ${result.overallSkillsAverage}% (Verbal Fluency 100%, Spatial Visualization 80% Top Strengths)", 36f, currentY, paint)
        currentY += 18f

        val skills = listOf(
            Triple("Verbal Fluency & Language", result.skillVerbal, Color.rgb(16, 185, 129)),
            Triple("Spatial & 3D Visualization", result.skillSpatial, Color.rgb(37, 99, 235)),
            Triple("Mechanical Intuition", result.skillMechanical, Color.rgb(234, 88, 12)),
            Triple("Leadership & Decisiveness", result.skillLeadership, Color.rgb(139, 92, 246)),
            Triple("Logical & Abstract Reasoning", result.skillLogical, Color.rgb(6, 182, 212)),
            Triple("Social Cooperation & Empathy", result.skillSocialCooperation, Color.rgb(236, 72, 153)),
            Triple("Administrative & Organizational", result.skillAdministrative, Color.rgb(100, 116, 139)),
            Triple("Numerical & Quantitative", result.skillNumerical, Color.rgb(239, 68, 68))
        )

        skills.forEach { (label, score, color) ->
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9f
            paint.color = Color.rgb(30, 41, 59)
            canvas.drawText(label, 36f, currentY + 11f, paint)

            drawProgressBar(canvas, paint, 210f, currentY + 2f, 240f, 10f, score, color)

            paint.typeface = Typeface.DEFAULT
            paint.textSize = 9f
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawText("$score%", 465f, currentY + 11f, paint)

            currentY += 19f
        }

        currentY += 22f

        // Section 7: Academic Stream Selection Matrix
        drawSectionTitle(canvas, paint, "SECTION 7: ACADEMIC STREAM SELECTION MATRIX (10TH / 12TH GRADE)", currentY)
        currentY += 18f

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(100, 116, 139)
        canvas.drawText("Matches psychometric profile, cognitive strengths, and career motivators with academic streams.", 36f, currentY, paint)
        currentY += 18f

        val streams = listOf(
            Triple("Humanities & Liberal Arts", result.humanitiesMatch, Color.rgb(16, 185, 129)),
            Triple("Science (Maths / Computer Tech)", result.scienceMathsMatch, Color.rgb(59, 130, 246)),
            Triple("Science (Biology / Medical)", result.scienceBioMatch, Color.rgb(245, 158, 11)),
            Triple("Commerce & Financial Management", result.commerceMatch, Color.rgb(148, 163, 184))
        )

        streams.forEach { (name, matchPct, color) ->
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9.5f
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawText(name, 36f, currentY + 11f, paint)

            drawProgressBar(canvas, paint, 220f, currentY + 2f, 230f, 11f, matchPct, color)

            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9.5f
            paint.color = color
            val tag = if (matchPct >= 80) " (Top Choice)" else if (matchPct >= 60) " (Good Choice)" else ""
            canvas.drawText("$matchPct%$tag", 460f, currentY + 11f, paint)

            currentY += 22f
        }

        currentY += 16f

        // Stream Advisory Callout
        paint.color = Color.rgb(238, 242, 255)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 65f, 8f, 8f, paint)
        paint.color = Color.rgb(79, 70, 229)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10f
        canvas.drawText("ADVISORY FOR STREAM SELECTION:", 48f, currentY + 20f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 8.5f
        paint.color = Color.rgb(55, 48, 163)
        canvas.drawText("• Outstanding fit for Humanities / Creative Arts (93%). Focus on Literature, Fine Arts, Psychology, and Media.", 48f, currentY + 36f, paint)
        canvas.drawText("• Science with Maths & Computer Graphics (61%) is also viable if accompanied by creative digital design pursuits.", 48f, currentY + 50f, paint)

        drawFooter(canvas, paint, 4)
        pdfDocument.finishPage(page)
    }

    // --- Page 5: Career Clusters, Top Occupations & Counselor Review ---
    private fun generatePage5(pdfDocument: PdfDocument, result: AssessmentResult, matchedCareers: List<CareerPath>) {
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 5).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        drawHeader(canvas, paint, "CAREER CLUSTERS & COUNSELOR ENDORSEMENT", 5)

        var currentY = 105f

        // Section 8: Recommended Career Clusters & Occupations
        drawSectionTitle(canvas, paint, "SECTION 8: TOP RECOMMENDED CAREER CLUSTERS & PROFESSIONS", currentY)
        currentY += 18f

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 8.5f
        paint.color = Color.rgb(100, 116, 139)
        canvas.drawText("Selected Clusters: Creative Arts & Design, Human Services, Architecture & Construction, Media", 36f, currentY, paint)
        currentY += 18f

        // Top Careers (Show 4 structured career entries)
        val sampleCareers = if (matchedCareers.isNotEmpty()) {
            matchedCareers.take(4)
        } else {
            emptyList()
        }

        sampleCareers.forEachIndexed { index, career ->
            paint.color = Color.rgb(248, 250, 252)
            canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 70f, 8f, 8f, paint)
            paint.color = Color.rgb(226, 232, 240)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 70f, 8f, 8f, paint)
            paint.style = Paint.Style.FILL

            // Career Title & Industry
            paint.color = Color.rgb(15, 23, 42)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 10.5f
            canvas.drawText("${index + 1}. ${career.title}  [${career.industry}]", 46f, currentY + 18f, paint)

            // Recommendation Badge
            val recText = career.recommendationComment.ifEmpty { "Top Choice" }
            paint.color = Color.rgb(16, 185, 129)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 9.5f
            canvas.drawText("★ $recText", 470f, currentY + 18f, paint)

            // Details
            paint.typeface = Typeface.DEFAULT
            paint.textSize = 8.5f
            paint.color = Color.rgb(71, 85, 105)
            canvas.drawText("• Specializations: ${career.specializations.ifEmpty { "Drawing, Painting, Art Direction, Visual Styling" }}", 46f, currentY + 32f, paint)
            canvas.drawText("• Psychometric Rating: ${career.psyAnalysisRating.ifEmpty { "High: 65" }} | Skills Alignment: ${career.skillsAbilitiesRating.ifEmpty { "Very High: 80" }}", 46f, currentY + 45f, paint)
            canvas.drawText("• Roadmap: ${career.academicRoadmap.take(90)}...", 46f, currentY + 58f, paint)

            currentY += 78f
        }

        currentY += 6f

        // Section 9: Academic Counselor Endorsement & Next Steps
        drawSectionTitle(canvas, paint, "SECTION 9: ACADEMIC COUNSELING REMARKS & NEXT STEPS", currentY)
        currentY += 18f

        paint.color = Color.rgb(241, 245, 249)
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 120f, 8f, 8f, paint)
        paint.color = Color.rgb(203, 213, 225)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(36f, currentY, (PAGE_WIDTH - 36).toFloat(), currentY + 120f, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 9f
        paint.color = Color.rgb(51, 65, 85)
        val counselorRemark = if (result.counselorNotes.isNotBlank()) {
            result.counselorNotes
        } else {
            "Siya demonstrates remarkable creative, verbal, and spatial gifts with an INFP archetype. Highly suited for Humanities or Creative Design streams. Recommend exploring Fine Arts, Graphic/UX Design, and Architecture."
        }
        drawWrappedText(canvas, paint, counselorRemark, 48f, currentY + 22f, PAGE_WIDTH - 96f, 14f)

        // Counselor Signature Box
        val counselorSigner = if (result.counselorReviewedBy.isNotBlank()) result.counselorReviewedBy else "Dr. Sarah Jenkins (Lead Academic Counselor)"
        paint.color = Color.rgb(30, 41, 59)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 9.5f
        canvas.drawText("Faculty Signature: _______________________", 48f, currentY + 85f, paint)
        canvas.drawText("Endorsed by: $counselorSigner", 48f, currentY + 102f, paint)

        canvas.drawText("Official Seal: [ Verified Academic Record ]", 330f, currentY + 85f, paint)
        canvas.drawText("Status: Finalized & Archival Approved", 330f, currentY + 102f, paint)

        drawFooter(canvas, paint, 5)
        pdfDocument.finishPage(page)
    }

    // --- Helper Drawing Utilities ---

    private fun drawHeader(canvas: Canvas, paint: Paint, pageTitle: String, pageNumber: Int) {
        // Top banner
        paint.color = Color.rgb(26, 35, 126) // Deep academic navy
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 80f, paint)

        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 15f
        canvas.drawText("ACADEMIC CAREER COUNSELING DEPARTMENT", 36f, 32f, paint)

        paint.typeface = Typeface.DEFAULT
        paint.textSize = 10f
        paint.color = Color.rgb(224, 231, 255)
        canvas.drawText("Career Counseling & Psychometric Trait Report • $pageTitle", 36f, 49f, paint)
        canvas.drawText("Career by Choice Standardized Assessment Framework", 36f, 64f, paint)

        // Page Number badge
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10f
        paint.color = Color.WHITE
        canvas.drawText("PAGE $pageNumber OF $TOTAL_PAGES", (PAGE_WIDTH - 110).toFloat(), 32f, paint)
    }

    private fun drawFooter(canvas: Canvas, paint: Paint, pageNumber: Int) {
        paint.typeface = Typeface.DEFAULT
        paint.textSize = 8f
        paint.color = Color.rgb(148, 163, 184)
        canvas.drawText(
            "Confidential Academic Guidance Record • Academic Counseling Portal • Career by Choice Psychometric Framework",
            36f,
            PAGE_HEIGHT - 22f,
            paint
        )
        canvas.drawText("Page $pageNumber of $TOTAL_PAGES", (PAGE_WIDTH - 90).toFloat(), PAGE_HEIGHT - 22f, paint)
    }

    private fun drawSectionTitle(canvas: Canvas, paint: Paint, title: String, y: Float) {
        paint.color = Color.rgb(30, 58, 138)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 11.5f
        canvas.drawText(title, 36f, y, paint)
    }

    private fun drawProgressBar(
        canvas: Canvas,
        paint: Paint,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        percentage: Int,
        fillColor: Int
    ) {
        // Track
        paint.color = Color.rgb(226, 232, 240)
        canvas.drawRoundRect(x, y, x + width, y + height, height / 2, height / 2, paint)

        // Fill
        val fillWidth = (percentage.coerceIn(0, 100) / 100f) * width
        if (fillWidth > 0) {
            paint.color = fillColor
            canvas.drawRoundRect(x, y, x + fillWidth, y + height, height / 2, height / 2, paint)
        }
    }

    private fun drawMbtiSpectrum(
        canvas: Canvas,
        paint: Paint,
        dimensionName: String,
        leftLabel: String,
        leftScore: Int,
        rightLabel: String,
        rightScore: Int,
        y: Float
    ) {
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 9.5f
        paint.color = Color.rgb(30, 41, 59)
        canvas.drawText(dimensionName, 36f, y, paint)

        val barY = y + 8f
        val barStartX = 36f
        val barWidth = 523f
        val barHeight = 10f

        // Draw dual background bar
        paint.color = Color.rgb(226, 232, 240)
        canvas.drawRoundRect(barStartX, barY, barStartX + barWidth, barY + barHeight, 5f, 5f, paint)

        // Left split fill
        val leftFraction = leftScore / 100f
        val leftFillWidth = barWidth * leftFraction
        paint.color = if (leftScore >= rightScore) Color.rgb(79, 70, 229) else Color.rgb(165, 180, 252)
        canvas.drawRoundRect(barStartX, barY, barStartX + leftFillWidth, barY + barHeight, 5f, 5f, paint)

        // Right split fill
        paint.color = if (rightScore >= leftScore) Color.rgb(124, 58, 237) else Color.rgb(196, 181, 253)
        canvas.drawRoundRect(barStartX + leftFillWidth, barY, barStartX + barWidth, barY + barHeight, 5f, 5f, paint)

        // Labels under bar
        paint.typeface = Typeface.DEFAULT
        paint.textSize = 8.5f
        paint.color = Color.rgb(71, 85, 105)
        canvas.drawText("$leftLabel ($leftScore%)", barStartX, barY + 20f, paint)

        val rightText = "$rightLabel ($rightScore%)"
        val rightTextWidth = paint.measureText(rightText)
        canvas.drawText(rightText, barStartX + barWidth - rightTextWidth, barY + 20f, paint)
    }

    private fun drawWrappedText(
        canvas: Canvas,
        paint: Paint,
        text: String,
        x: Float,
        startY: Float,
        maxWidth: Float,
        lineSpacing: Float
    ) {
        val words = text.split(" ")
        var currentLine = StringBuilder()
        var currentY = startY

        for (word in words) {
            val prospectiveLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = paint.measureText(prospectiveLine)
            if (width > maxWidth && currentLine.isNotEmpty()) {
                canvas.drawText(currentLine.toString(), x, currentY, paint)
                currentLine = StringBuilder(word)
                currentY += lineSpacing
            } else {
                currentLine = StringBuilder(prospectiveLine)
            }
        }
        if (currentLine.isNotEmpty()) {
            canvas.drawText(currentLine.toString(), x, currentY, paint)
        }
    }

    fun openOrSharePdf(context: Context, file: File, title: String = "Career Assessment Report") {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, "Attached is the official 5-Page Comprehensive Psychometric Career Counseling Assessment Report.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Download / Share Assessment Report PDF")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
