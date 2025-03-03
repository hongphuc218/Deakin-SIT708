import { useState } from "react";

const unitCategories = {
  length: { cm: 1, inch: 2.54, foot: 30.48, yard: 91.44, mile: 160934 },
  weight: { kg: 1, pound: 0.453592, ounce: 0.0283495, ton: 907.185 },
  temperature: { Celsius: "C", Fahrenheit: "F", Kelvin: "K" },
};

const convertTemperature = (value: number, from: string, to: string): number | null => {
  if (from === to) return value;
  switch (from) {
    case "Celsius": return to === "Fahrenheit" ? (value * 1.8) + 32 : value + 273.15;
    case "Fahrenheit": return to === "Celsius" ? (value - 32) / 1.8 : ((value - 32) / 1.8) + 273.15;
    case "Kelvin": return to === "Celsius" ? value - 273.15 : ((value - 273.15) * 1.8) + 32;
    default: return null;
  }
};

const convertValue = (value: number, from: string, to: string, category: keyof typeof unitCategories): number | null => {
  if (category === "temperature") return convertTemperature(value, from, to);
  const fromFactor = unitCategories[category][from as keyof typeof unitCategories[typeof category]];
  const toFactor = unitCategories[category][to as keyof typeof unitCategories[typeof category]];
  return (value * fromFactor) / toFactor;
};

const UnitConverter = () => {
  const [category, setCategory] = useState<keyof typeof unitCategories>("length");
  const [sourceUnit, setSourceUnit] = useState(Object.keys(unitCategories.length)[0]);
  const [destinationUnit, setDestinationUnit] = useState(Object.keys(unitCategories.length)[1]);
  const [sourceValue, setSourceValue] = useState("");
  const [destinationValue, setDestinationValue] = useState("");
  const [error, setError] = useState<string | null>(null);

  const resetFields = () => {
    setSourceValue("");
    setDestinationValue("");
    setError(null);
  };

  const validateInput = (value: string, unit: string): string | null => {
    if (value.trim() === "") return null;

    const numericValue = parseFloat(value);
    if (isNaN(numericValue)) return "Please enter a valid number.";

    if ((category === "length" || category === "weight") && numericValue < 0) {
      return "Please enter a valid positive number.";
    }

    if (category === "temperature" && unit === "Kelvin" && numericValue < 0) {
      return "Kelvin must be ≥ 0. Negative values are not valid in this scale.";
    }

    return null;
  };

  const handleInputChange = (value: string, type: "source" | "destination") => {
    if (type === "source") {
      setSourceValue(value);
    } else {
      setDestinationValue(value);
    }

    const unit = type === "source" ? sourceUnit : destinationUnit;
    const validationError = validateInput(value, unit);
    setError(validationError);

    if (!validationError && value.trim() !== "") {
      const numericValue = parseFloat(value);
      const converted = type === "source"
        ? convertValue(numericValue, sourceUnit, destinationUnit, category)
        : convertValue(numericValue, destinationUnit, sourceUnit, category);

      if (type === "source") {
        setDestinationValue(converted !== null ? converted.toFixed(2) : "");
      } else {
        setSourceValue(converted !== null ? converted.toFixed(2) : "");
      }
    }
  };

  const handleUnitChange = (type: "source" | "destination", newUnit: string) => {
    if (type === "source") {
      setSourceUnit(newUnit);
    } else {
      setDestinationUnit(newUnit);
    }
    resetFields(); // Clear fields when unit changes
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-base-100 p-6">
      <div className="card w-full max-w-xl bg-base-200 shadow-xl p-6">
        <h2 className="text-4xl font-bold text-center">Unit Converter</h2>

        {/* Category Selector */}
        <div className="form-control mt-4">
          <label className="label">
            <span className="label-text">Select Category</span>
          </label>
          <select
            className="select select-xl select-bordered w-full"
            value={category}
            onChange={(e) => {
              const newCategory = e.target.value as keyof typeof unitCategories;
              setCategory(newCategory);
              setSourceUnit(Object.keys(unitCategories[newCategory])[0]);
              setDestinationUnit(Object.keys(unitCategories[newCategory])[1]);
              resetFields(); // Reset when category changes
            }}
          >
            {Object.keys(unitCategories).map((key) => (
              <option key={key} value={key}>{key}</option>
            ))}
          </select>
        </div>

        {/* Input Fields with Two-Way Conversion */}
        {(["source", "destination"] as const).map((type) => (
          <div key={type} className="form-control mt-4">
            <label className="label">
              <span className="label-text">{type === "source" ? "From" : "To"}</span>
            </label>
            <div className="flex gap-2">
              <input
                type="number"
                className={`input input-xl ${error ? "input-error" : "input-bordered"} flex-1`}
                value={type === "source" ? sourceValue : destinationValue}
                onChange={(e) => handleInputChange(e.target.value, type)}
                placeholder="Enter value"
              />
              <select
                className="select select-xl select-bordered"
                value={type === "source" ? sourceUnit : destinationUnit}
                onChange={(e) => handleUnitChange(type, e.target.value)}
              >
                {Object.keys(unitCategories[category]).map((unit) => (
                  <option key={unit} value={unit}>{unit}</option>
                ))}
              </select>
            </div>
          </div>
        ))}

        {/* Error Message Display */}
        {error && (
          <div className="mt-2">
            <div className="alert alert-error p-2 text-white">
              ⚠ {error}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default UnitConverter;
