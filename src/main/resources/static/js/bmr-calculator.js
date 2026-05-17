document.addEventListener('DOMContentLoaded', () => {
    const heightInput = document.getElementById('height');
    const weightInput = document.getElementById('weight');
    const ageInput = document.getElementById('age');
    const genderInput = document.getElementById('gender');
    const activityInput = document.getElementById('activityLevel');
    const goalInput = document.getElementById('fitnessGoal');

    const bmrOutput = document.getElementById('bmrOutput');
    const maintenanceOutput = document.getElementById('maintenanceOutput');
    const weightLossOutput = document.getElementById('weightLossOutput');
    const weightGainOutput = document.getElementById('weightGainOutput');
    const proteinOutput = document.getElementById('proteinOutput');
    const waterOutput = document.getElementById('waterOutput');
    
    // Hidden inputs to submit calculated values
    const bmrHidden = document.getElementById('hiddenBmr');
    const maintenanceHidden = document.getElementById('hiddenMaintenance');
    const weightLossHidden = document.getElementById('hiddenWeightLoss');
    const weightGainHidden = document.getElementById('hiddenWeightGain');
    const proteinHidden = document.getElementById('hiddenProtein');
    const waterHidden = document.getElementById('hiddenWater');

    function calculateBmr() {
        if (!heightInput || !weightInput || !ageInput || !genderInput || !activityInput) return;

        const height = parseFloat(heightInput.value);
        const weight = parseFloat(weightInput.value);
        const age = parseInt(ageInput.value);
        const gender = genderInput.value;
        const activity = activityInput.value;
        
        if (isNaN(height) || isNaN(weight) || isNaN(age) || height <= 0 || weight <= 0 || age <= 0) {
            return; // Not ready to calculate
        }

        // Mifflin-St Jeor Equation
        let bmr = (10 * weight) + (6.25 * height) - (5 * age);
        if (gender === 'Male') {
            bmr += 5;
        } else {
            bmr -= 161;
        }
        
        let multiplier = 1.2;
        switch (activity) {
            case "Lightly Active": multiplier = 1.375; break;
            case "Moderately Active": multiplier = 1.55; break;
            case "Very Active": multiplier = 1.725; break;
            case "Extra Active": multiplier = 1.9; break;
            case "Sedentary":
            default: multiplier = 1.2; break;
        }

        const maintenance = Math.round(bmr * multiplier);
        const weightLoss = maintenance - 500;
        const weightGain = maintenance + 500;
        const protein = Math.round(weight * 2.2);
        const water = Math.round(weight * 0.033);
        const finalBmr = Math.round(bmr);

        // Animate numbers
        animateValue(bmrOutput, parseInt(bmrOutput.innerText) || 0, finalBmr, 1000, ' kcal');
        animateValue(maintenanceOutput, parseInt(maintenanceOutput.innerText) || 0, maintenance, 1000, ' kcal');
        animateValue(weightLossOutput, parseInt(weightLossOutput.innerText) || 0, weightLoss, 1000, ' kcal');
        animateValue(weightGainOutput, parseInt(weightGainOutput.innerText) || 0, weightGain, 1000, ' kcal');
        animateValue(proteinOutput, parseInt(proteinOutput.innerText) || 0, protein, 1000, 'g');
        animateValue(waterOutput, parseInt(waterOutput.innerText) || 0, water, 1000, 'L');
        
        // Set hidden fields
        if(bmrHidden) bmrHidden.value = finalBmr;
        if(maintenanceHidden) maintenanceHidden.value = maintenance;
        if(weightLossHidden) weightLossHidden.value = weightLoss;
        if(weightGainHidden) weightGainHidden.value = weightGain;
        if(proteinHidden) proteinHidden.value = protein;
        if(waterHidden) waterHidden.value = water;
    }

    function animateValue(obj, start, end, duration, suffix = '') {
        if (!obj) return;
        let startTimestamp = null;
        const step = (timestamp) => {
            if (!startTimestamp) startTimestamp = timestamp;
            const progress = Math.min((timestamp - startTimestamp) / duration, 1);
            obj.innerHTML = Math.floor(progress * (end - start) + start) + suffix;
            if (progress < 1) {
                window.requestAnimationFrame(step);
            }
        };
        window.requestAnimationFrame(step);
    }

    // Add event listeners
    [heightInput, weightInput, ageInput, genderInput, activityInput, goalInput].forEach(input => {
        if(input) {
            input.addEventListener('input', calculateBmr);
            input.addEventListener('change', calculateBmr);
        }
    });

    // Initial calculation if values exist
    if (heightInput && heightInput.value && weightInput && weightInput.value) {
        calculateBmr();
    }
});
