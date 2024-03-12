const useFormValidation = () => {
  const validateModel = (model) => {
    return model.trim().length < 2 || model.trim().length > 20 ? 'Model must be between 2 and 20 characters' : '';
  };

  const validateBrand = (brand) => {
    return brand.trim().length < 2 || brand.trim().length > 20 ? 'Brand must be between 2 and 20 characters' : '';
  };

  const validateInventory = (inventory) => {
    return inventory.trim().length < 2 || inventory.trim().length > 20 ? 'Inventory must be between 2 and 20 characters' : '';
  };

  const validateDailyFee = (dailyFee) => {
    return dailyFee.trim().length < 2 || dailyFee.trim().length > 20 ? 'Daily fee must be between 2 and 20 characters' : '';
  };

  return {
    validateModel,
    validateBrand,
    validateInventory,
    validateDailyFee,
  };
};

export default useFormValidation;